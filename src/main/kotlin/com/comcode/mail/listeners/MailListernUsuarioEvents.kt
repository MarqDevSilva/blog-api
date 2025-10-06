package com.comcode.mail.listeners

import com.comcode.mail.MailService
import com.comcode.domains.usuario.events.UsuarioSignUpEvent
import io.micronaut.runtime.event.annotation.EventListener
import jakarta.inject.Singleton
import com.comcode.utils.EmailUtils.buildEmailTemplate
import io.micronaut.context.annotation.Value
import io.micronaut.security.token.generator.TokenGenerator

@Singleton
class MailListernUsuarioEvents(
    private val mailService: MailService,
    private val tokenGenerator: TokenGenerator,
    @Value("\${urls.verify-email}") private val urlVerifyEmail: String
){

    @EventListener
    fun usuarioSignUpEvent(event: UsuarioSignUpEvent) {

        val token = tokenGenerator.generateToken(event.authentication, 3600 )
            .orElseThrow { RuntimeException("Erro ao gerar token de verificação de email") }

        println("Link de verificação: ${urlVerifyEmail}${token}")

        val emailRequest = com.comcode.mail.dto.EmailRequest(
            to = event.authentication.name,
            subject = "Bem-vindo ao ComCode!",
            body = buildEmailTemplate("""
                <h1>Bem-vindo!</h1>
                <p>Estamos muito felizes por ter você em nossa comunidade.</p>
                <p>Agora você está pronto para explorar tudo o que nossa plataforma tem a oferecer. Clique no botão abaixo para confirmar seu email.</p>
                <div align="center">
                    <a href="${urlVerifyEmail}${token}" class="cta-button">Confirmar email</a>
                </div>
                <p>Se precisar de ajuda, nossa equipe de suporte está sempre à disposição.</p>
            """.trimIndent())
        )
        mailService.sendEmail(emailRequest)
    }
}