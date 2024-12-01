package com.iplastudio.boilerplate.features.emails

import com.iplastudio.boilerplate.features.emails.models.TemplatedEmail
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class NoOpEmailService : EmailService {

    private val log = LoggerFactory.getLogger(NoOpEmailService::class.java)

    override fun sendEmail(email: TemplatedEmail, context: Any) {
        log.info("Request to send email {} with context {}", email, context)
    }
}