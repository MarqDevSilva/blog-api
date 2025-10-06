package com.comcode.domains.comment.dto

import com.comcode.domains.post.dto.PostIdDto
import com.fasterxml.jackson.annotation.JsonProperty
import com.comcode.domains.usuario.dto.UsuarioDto

data class CommentDto(
    val id: Long? = null,
    val content: String,
    val author: UsuarioDto,

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    val post: PostIdDto,

    val parent: CommentIdDto?,
    val replies: List<CommentIdDto>
)