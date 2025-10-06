package com.comcode.mail

import com.comcode.exceptions.EmailSendException
import com.comcode.mail.dto.EmailRequest
import com.comcode.utils.EmailUtils.isHtml
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.gmail.GmailScopes
import com.google.api.services.gmail.Gmail
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton
import jakarta.mail.Message
import jakarta.mail.Session
import jakarta.mail.internet.MimeMessage
import jakarta.mail.internet.InternetAddress
import java.io.ByteArrayOutputStream
import java.util.Base64

@Singleton
class MailService (
    @Value("\${gcp.gmail.delegated-user}") private val delegatedUser: String,
    @Value("\${gcp.gmail.application-name}") private val applicationName: String,
    @Value("\${gcp.credentials.location}") private var credentialsLocation: String
){

    /**
     * Cria uma instância do serviço Gmail usando:
     *
     * - Escopo de envio de e-mails (GMAIL_SEND)
     * - Delegação de domínio para o usuário delegado (noreply@comcode.com.br)
     */
    private fun getGmailService(): Gmail {
        val credentialsStream = java.io.FileInputStream(credentialsLocation)

        val credentials = GoogleCredentials.fromStream(credentialsStream)
            .createScoped(GmailScopes.GMAIL_SEND)
            .createDelegated(delegatedUser)

        return Gmail.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            GsonFactory.getDefaultInstance(),
            HttpCredentialsAdapter(credentials)
        )
            .setApplicationName(applicationName)
            .build()
    }

    /**
     * Envia um e-mail.
     * @param to destinatário
     * @param subject assunto
     * @param body corpo do e-mail
     * @param from remetente opcional; se não informado, usa delegatedUser.
     *             OBS: o remetente deve ser um alias autorizado da conta ADC (Application Default Credentials),
     *
     */
    fun sendEmail(emailRequest: EmailRequest) {
        try {
            val email = MimeMessage(Session.getDefaultInstance(System.getProperties()))

            email.setFrom(InternetAddress(emailRequest.from ?: delegatedUser))
            email.addRecipient(Message.RecipientType.TO, InternetAddress(emailRequest.to))
            email.subject = emailRequest.subject
            email.setContent(emailRequest.body, if (isHtml(emailRequest.body)) "text/html; charset=UTF-8" else "text/plain; charset=UTF-8")

            val buffer = ByteArrayOutputStream()

            email.writeTo(buffer)

            getGmailService().users().messages().send("me",
                com.google.api.services.gmail.model.Message()
                    .setRaw(Base64.getUrlEncoder().encodeToString(buffer.toByteArray()))
            ).execute()
        }catch (e: Exception) {
            throw EmailSendException("Erro ao enviar e-mail para ${emailRequest.to}", e)
        }
    }
}