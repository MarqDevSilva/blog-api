package com.comcode.domains.post.dto

import com.comcode.domains.collection.dto.CollectionDto
import com.comcode.domains.technology.dto.TechnologyIdDto
import com.comcode.domains.usuario.dto.UsuarioIdDto
import com.comcode.enums.StatusPost
import java.time.LocalDate

data class PostDto(
    val id: Long?,
    val author: UsuarioIdDto,
    val collection: CollectionDto?,
    val technologies: List<TechnologyIdDto>,
    val slug: String,
    val summary: Short,
    val content: String,
    val contentHtml: String,
    val status: StatusPost,
    val publishedAt: LocalDate,
    val metaTitle: String,
    val metaDescription: String,
    val canonicalUrl: String?,
    val viewsCount: Long,
    val likesCount: Long
)
