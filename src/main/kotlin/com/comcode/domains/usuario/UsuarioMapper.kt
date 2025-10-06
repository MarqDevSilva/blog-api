package com.comcode.domains.usuario

import org.comcode.config.MapperConfigBase
import com.comcode.domains.usuario.dto.UsuarioDto
import com.comcode.interfaces.EntityMapper
import org.mapstruct.Mapper

@Mapper(config = MapperConfigBase::class)
interface UsuarioMapper : EntityMapper<UsuarioDto, UsuarioModel>{}
