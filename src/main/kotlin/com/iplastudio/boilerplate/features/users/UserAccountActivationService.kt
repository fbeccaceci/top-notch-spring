package com.iplastudio.boilerplate.features.users

import com.iplastudio.boilerplate.exceptions.EntityNotFoundException
import com.iplastudio.boilerplate.features.emails.EmailService
import com.iplastudio.boilerplate.features.emails.models.TemplatedEmail
import com.iplastudio.boilerplate.features.users.exceptions.UserAlreadyActiveException
import com.iplastudio.boilerplate.utils.TokenGenerator
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserAccountActivationService(
    private val emailService: EmailService,
    private val userOtpRepository: UserOtpRepository,
    private val userRepository: UserRepository
) {

    private val log = LoggerFactory.getLogger(UserAccountActivationService::class.java)

    fun beginAccountActivation(user: User) {
        if (user.status != User.Status.WAITING_EMAIL_VERIFICATION) {
            throw UserAlreadyActiveException()
        }

        log.info("Beginning account activation for ${user.username}")

        userOtpRepository.findByUserIdAndTypeAndStatus(
            user.id!!,
            UserOtp.Type.ACCOUNT_ACTIVATION,
            UserOtp.Status.VALID
        )?.let {
            it.status = UserOtp.Status.REPLACED
            userOtpRepository.save(it)
        }

        val otp = userOtpRepository.save(
            UserOtp(
                userId = user.id!!,
                type = UserOtp.Type.ACCOUNT_ACTIVATION,
                status = UserOtp.Status.VALID,
                otpCode = TokenGenerator.otpCode(),
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
        val userOtp = userOtpRepository.findByOtpCodeAndTypeAndStatus(
            otp,
            UserOtp.Type.ACCOUNT_ACTIVATION,
            UserOtp.Status.VALID
        ) ?: throw EntityNotFoundException(UserOtp::class)

        userOtp.status = UserOtp.Status.USED
        userOtpRepository.save(userOtp)

        val user = userRepository.findByIdOrNull(userOtp.userId)
            ?: throw EntityNotFoundException(User::class)
        user.status = User.Status.ACTIVE
        user.emailVerifiedAt = LocalDateTime.now()
        userRepository.save(user)

        log.info("Account activated for ${user.username}")
    }

}