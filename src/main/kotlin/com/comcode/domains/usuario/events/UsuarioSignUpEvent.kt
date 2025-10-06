package com.comcode.domains.usuario.events

import io.micronaut.security.authentication.Authentication

data class UsuarioSignUpEvent(val authentication: Authentication)
