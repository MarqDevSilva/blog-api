package com.comcode.authentication

import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationFailureReason
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.authentication.provider.AuthenticationProvider
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.mindrot.jbcrypt.BCrypt
import com.comcode.domains.usuario.UsuarioModel
import com.comcode.domains.usuario.UsuarioRepository

@Singleton
class AuthenticationProvider @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) : AuthenticationProvider<HttpRequest<*>, String, String> {

    override fun authenticate(
        requestContext: HttpRequest<*>?,
        authRequest: AuthenticationRequest<String, String>
    ): AuthenticationResponse {
        val usuario: UsuarioModel = usuarioRepository.findByEmail(authRequest.identity)
            ?: return AuthenticationResponse.failure(AuthenticationFailureReason.USER_NOT_FOUND)

        if (!BCrypt.checkpw(authRequest.secret, usuario.password)) {
            return AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH)
        }

        val claims = mapOf(
            "id" to usuario.id
        )

        return AuthenticationResponse.success(authRequest.identity, listOf(usuario.role?.name), claims)
    }
}