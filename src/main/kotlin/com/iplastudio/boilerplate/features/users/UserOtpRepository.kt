package com.iplastudio.boilerplate.features.users

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserOtpRepository: JpaRepository<UserOtp, UUID> {

    fun findByUserIdAndTypeAndStatus(userId: UUID, type: UserOtp.Type, status: UserOtp.Status): UserOtp?

    fun findByOtpCodeAndTypeAndStatus(otpCode: String, type: UserOtp.Type, status: UserOtp.Status): UserOtp?

}