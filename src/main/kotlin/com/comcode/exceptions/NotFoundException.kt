package com.comcode.exceptions

/**
 * Exceção customizada para indicar que um recurso não foi encontrado.
 * Permite definir uma mensagem personalizada para a exceção.
 *
 * @param message A mensagem detalhada que descreve o recurso não encontrado.
 */
class NotFoundException(message: String) : RuntimeException(message)