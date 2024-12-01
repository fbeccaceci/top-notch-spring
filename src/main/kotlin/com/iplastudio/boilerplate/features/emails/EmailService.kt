package com.iplastudio.boilerplate.features.emails

import com.iplastudio.boilerplate.features.emails.models.TemplatedEmail

interface EmailService {

    fun sendEmail(email: TemplatedEmail, context: Any)

}