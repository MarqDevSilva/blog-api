package com.comcode.domains.usuario.dto

data class LoginResponse(
    val username: String,
    val roles: List<String>,
    val access_token: String,
    val token_type: String,
    val expires_in: Int
)
