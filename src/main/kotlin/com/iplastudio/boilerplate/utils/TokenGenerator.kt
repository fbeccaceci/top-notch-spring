package com.iplastudio.boilerplate.utils

import kotlin.random.Random

object TokenGenerator {

    private const val CHAR_POOL: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    private const val OTP_CHAR_POOL: String = "0123456789"

    fun generateToken(length: Int = 16): String {
        return (1..length)
            .map { Random.nextInt(0, CHAR_POOL.length) }
            .map(CHAR_POOL::get)
            .joinToString("")
    }

    fun otpCode(length: Int = 6): String {
        return (1..length)
            .map { Random.nextInt(0, OTP_CHAR_POOL.length) }
            .map(OTP_CHAR_POOL::get)
            .joinToString("")
    }
}
