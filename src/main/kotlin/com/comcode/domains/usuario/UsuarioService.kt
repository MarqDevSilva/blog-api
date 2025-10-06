package com.comcode.domains.usuario

import com.comcode.exceptions.AlreadyExistsException
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton
import jakarta.transaction.Transactional
import com.comcode.domains.usuario.dto.UsuarioDto
import com.comcode.templates.GenericServiceTemplate

@Singleton
open class UsuarioService(
    override val repository: UsuarioRepository,
    override val mapper: UsuarioMapper,
) : GenericServiceTemplate<UsuarioModel, Long, UsuarioDto>(repository, repository, mapper), IUsuarioService {

    @Transactional
    override fun signup(dto: UsuarioDto): Authentication {
        if (repository.existsByEmail(dto.email)) {
            throw AlreadyExistsException("Email ${dto.email} já cadastrado")
        }

        val usuario: UsuarioModel = repository.saveAndFlush(mapper.toEntity(dto))

        return Authentication.build(
            usuario.email,
            listOfNotNull(usuario.role?.name),
            mapOf(
                "id" to usuario.id
            )
        )
    }

    fun updatePassword(email: String): Boolean {
        // Implementar lógica de recuperação de senha
        // Exemplo: enviar e-mail com link para redefinição de senha
        return true // Retornar true se a operação for bem-sucedida
    }

    fun resendVerificationEmail(email: String): Boolean {
        // Implementar lógica de reenvio de e-mail
        // Exemplo: verificar se o e-mail está cadastrado
        return repository.existsByEmail(email)
    }

    fun sendEmailRecoverPassword(email: String): Boolean {
        // Implementar lógica de envio de e-mail para recuperação de senha
        // Exemplo: enviar e-mail com instruções para redefinição de senha
        return true // Retornar true se o e-mail for enviado com sucesso
    }
}

