package com.iplastudio.boilerplate.features.users

import com.iplastudio.boilerplate.exceptions.EntityNotFoundException
import com.iplastudio.boilerplate.features.emails.EmailService
import com.iplastudio.boilerplate.features.emails.models.TemplatedEmail
import com.iplastudio.boilerplate.features.users.dtos.ConfirmPasswordResetRequest
import com.iplastudio.boilerplate.features.users.dtos.ResetPasswordRequest
import com.iplastudio.boilerplate.features.users.entities.User
import com.iplastudio.boilerplate.features.users.entities.UserOtp
import com.iplastudio.boilerplate.features.users.exceptions.UserNotEnabledException
import com.iplastudio.boilerplate.utils.TokenGenerator
import com.iplastudio.boilerplate.utils.plusMillis
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@ConfigurationProperties("app.users.password-reset")
data class PasswordResetProperties(
    /*
        The time in milliseconds after which the otp will expire
     */
    val otpExpirationTime: Long
)

@Service
class UserPasswordResetService(
    private val properties: PasswordResetProperties,
    private val emailService: EmailService,
    private val userRepository: UserRepository,
    private val userOtpService: UserOtpService,
    private val passwordEncoder: PasswordEncoder
) {
    private val log = LoggerFactory.getLogger(UserPasswordResetService::class.java)

    fun requestPasswordReset(request: ResetPasswordRequest) {
        // We allow in the email field to be either the username or the email
        val user = userRepository.findByEmailOrUsername(request.email, request.email)
            ?: throw EntityNotFoundException(User::class)

        if (user.status !== User.Status.ACTIVE) {
            throw UserNotEnabledException()
        }

        val otp = userOtpService.saveAndReplace(
            UserOtp(
                userId = user.id!!,
                type = UserOtp.Type.PASSWORD_RESET,
                otpCode = TokenGenerator.otpCode(),
                expiresAt = LocalDateTime.now().plusMillis(properties.otpExpirationTime)
            )
        )

        val verificationTemplatedEmail = TemplatedEmail(
            to = user.email,
            subject = "Reset your password",
            templateName = "emails/reset-password.html"
        )

        val ctx = mapOf("otp" to otp.otpCode)
        emailService.sendEmail(verificationTemplatedEmail, ctx)

        log.info("Password reset was successfully requested for user ${user.username}")
    }

    fun confirmPasswordReset(request: ConfirmPasswordResetRequest) {
        val userOtp = userOtpService.consume(request.otp, UserOtp.Type.PASSWORD_RESET)

        val user = userRepository.findByIdOrNull(userOtp.userId)
            ?: throw EntityNotFoundException(User::class)

        if (user.status !== User.Status.ACTIVE) {
            throw UserNotEnabledException()
        }

        user.password = passwordEncoder.encode(request.newPassword)

        userRepository.save(user)

        log.info("Password reset was successfully completed for user ${user.username}")
    }

}