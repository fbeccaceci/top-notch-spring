package com.iplastudio.boilerplate.features.users

import com.iplastudio.boilerplate.exceptions.EntityNotFoundException
import com.iplastudio.boilerplate.features.emails.EmailService
import com.iplastudio.boilerplate.features.emails.models.TemplatedEmail
import com.iplastudio.boilerplate.features.users.entities.User
import com.iplastudio.boilerplate.features.users.entities.UserOtp
import com.iplastudio.boilerplate.features.users.exceptions.UserAlreadyActiveException
import com.iplastudio.boilerplate.utils.TokenGenerator
import com.iplastudio.boilerplate.utils.plusMillis
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@ConfigurationProperties("app.users.account-activation")
data class SignupProperties(
    /*
        The time in milliseconds after which the otp will expire
     */
    val otpExpirationTime: Long
)


@Service
class UserAccountActivationService(
    private val emailService: EmailService,
    private val userRepository: UserRepository,
    private val userOtpService: UserOtpService,
    private val properties: SignupProperties
) {

    private val log = LoggerFactory.getLogger(UserAccountActivationService::class.java)

    fun beginAccountActivation(user: User) {
        if (user.status != User.Status.WAITING_EMAIL_VERIFICATION) {
            throw UserAlreadyActiveException()
        }

        log.info("Beginning account activation for ${user.username}")

        val otp = userOtpService.saveAndReplace(
            UserOtp(
                userId = user.id!!,
                type = UserOtp.Type.ACCOUNT_ACTIVATION,
                otpCode = TokenGenerator.otpCode(),
                expiresAt = LocalDateTime.now().plusMillis(properties.otpExpirationTime)
            )
        )

        val verificationTemplatedEmail = TemplatedEmail(
            to = user.email,
            subject = "Confirm your email",
            templateName = "emails/confirm-email.html"
        )

        val ctx = mapOf("otp" to otp.otpCode)
        emailService.sendEmail(verificationTemplatedEmail, ctx)
    }

    fun activateAccount(otp: String) {
        val userOtp = userOtpService.consume(otp, UserOtp.Type.ACCOUNT_ACTIVATION)

        val user = userRepository.findByIdOrNull(userOtp.userId)
            ?: throw EntityNotFoundException(User::class)
        user.status = User.Status.ACTIVE
        user.emailVerifiedAt = LocalDateTime.now()
        userRepository.save(user)

        log.info("Account activated for ${user.username}")
    }

}