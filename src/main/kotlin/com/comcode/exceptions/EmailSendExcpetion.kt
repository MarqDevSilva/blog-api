package com.comcode.exceptions

class EmailSendException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)