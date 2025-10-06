package com.comcode.exceptions.dto

data class ProblemDto(
    val type: String,
    val title: String,
    val status: String,
    val detail: String,
    val instance: String,
    val parameters: Map<String, Any>?
)
