package com.comcode.domains.collection.dto

import com.comcode.domains.usuario.dto.UsuarioIdDto

data class CollectionDto(
    val id: Long? = null,
    val author: UsuarioIdDto,
    val title: String,
    val slug: String,
    val posts: Long
)