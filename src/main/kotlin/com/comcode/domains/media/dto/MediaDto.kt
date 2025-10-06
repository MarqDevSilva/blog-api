package com.comcode.domains.media.dto

data class MediaDto(
    val id: Long? = null,
    val url: String,
    val fileName: String,
    val size: Long,
    val mimeType: String
)
