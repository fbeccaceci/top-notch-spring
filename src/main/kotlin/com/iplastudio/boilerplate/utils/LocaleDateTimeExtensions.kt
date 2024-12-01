package com.iplastudio.boilerplate.utils

import java.time.LocalDateTime

fun LocalDateTime.plusMillis(millis: Long): LocalDateTime {
    return this.plusNanos(millis * 1_000_000)
}