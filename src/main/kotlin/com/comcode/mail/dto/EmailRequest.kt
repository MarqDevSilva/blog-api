package com.comcode.mail.dto

data class EmailRequest(
    val to: String,
    val subject: String,
    val body: String,
    val from: String? = null,
    val name: String? = null
)
