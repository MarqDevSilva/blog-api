package com.comcode.domains.usuario

import io.micronaut.security.authentication.Authentication
import com.comcode.domains.usuario.dto.UsuarioDto
import com.comcode.interfaces.GenericService

interface IUsuarioService : GenericService<UsuarioModel, Long, UsuarioDto> {

    fun signup(dto: UsuarioDto): Authentication
}