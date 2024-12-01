package com.iplastudio.boilerplate.features.users

import com.iplastudio.boilerplate.exceptions.EntityNotFoundException
import com.iplastudio.boilerplate.features.users.exceptions.OtpExpiredException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserOtpService(
    private val userOtpRepository: UserOtpRepository
) {

    private val log = LoggerFactory.getLogger(UserOtpService::class.java)

    /**
     * Saves the otp and set the status of already exising Otps with the same userId and type to REPLACED
     */
    fun saveAndReplace(userOpt: UserOtp): UserOtp {
        userOtpRepository.findByUserIdAndTypeAndStatus(
            userOpt.userId,
            userOpt.type,
            UserOtp.Status.LATEST_CREATED
        )?.let {
            it.status = UserOtp.Status.REPLACED
            userOtpRepository.save(it)
        }

        val newOpt = userOtpRepository.save(userOpt)

        log.info("Created new Otp of type {} for user {}", userOpt.userId, newOpt.userId)

        return newOpt
    }

    /**
     * Consume an otp and asserts that it is of the expected type, in case it cannot find the requested otp,
     * it throws an exception
     */
    fun consume(otp: String, type: UserOtp.Type): UserOtp {
        val userOtp = userOtpRepository.findByOtpCodeAndTypeAndStatus(
            otp,
            type,
            UserOtp.Status.LATEST_CREATED
        ) ?: throw EntityNotFoundException(UserOtp::class)

        if (userOtp.expiresAt!!.isBefore(LocalDateTime.now())) {
            log.info("Tried to consumed Otp of type {} for user {} but was found expired", type, userOtp.userId)
            throw OtpExpiredException()
        }

        userOtp.status = UserOtp.Status.USED
        val consumedOtp = userOtpRepository.save(userOtp)

        log.info("Consumed Otp of type {} for user {}", type, consumedOtp.userId)

        return consumedOtp
    }

}