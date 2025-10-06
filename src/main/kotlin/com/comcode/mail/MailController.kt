package com.comcode.mail

import com.comcode.mail.dto.EmailRequest
import io.micronaut.http.annotation.Controller
import jakarta.inject.Inject
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Body
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@Controller("mail")
class MailController {

    @Inject
    lateinit var mailService: MailService

    @Post()
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun sendMail(@Body request: EmailRequest): String{
        mailService.sendEmail(request)
        return "E-mail enviado com sucesso"
    }
}