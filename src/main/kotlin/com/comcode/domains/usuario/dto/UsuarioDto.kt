package com.comcode.domains.usuario.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class UsuarioDto(
    val id: Long? = null,
    val nome: String,
    val email: String,
    val slug: String,
    val bio: String,

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    val password: String,
)
