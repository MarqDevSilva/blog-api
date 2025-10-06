package com.comcode.utils


import org.slf4j.Logger
import org.slf4j.LoggerFactory
import io.micronaut.http.HttpRequest
import java.util.UUID

/**
 * Utilitário para logar informações de exceções globais e genéricas no Micronaut.
 * Utiliza o LoggerFactory do SLF4J para criar e gerenciar os loggers.
 */
object LoggerUtils {

    // Logger específico para o GlobalExceptionHandler, para facilitar a filtragem nos logs.
    private val loggerGlobalHandler: Logger = LoggerFactory.getLogger(LoggerUtils::class.java)

    /**
     * Constrói e loga uma mensagem de erro detalhada para exceções tratadas globalmente,
     * incluindo um ID único para rastreamento.
     * @param ex A exceção ocorrida.
     * @param request A requisição HTTP associada à exceção.
     * @param errorId Um ID único para esta ocorrção de erro.
     */
    fun buildLogErrorGlobalHandler(ex: Exception, request: HttpRequest<*>, errorId: String) {
        loggerGlobalHandler.error(
            """
            ERRO [{}]
            Tipo........: {}
            Mensagem....: {}
            Método......: {}
            Path........: {}
            """,
            errorId,
            ex.javaClass.simpleName, // Nome simples da classe da exceção
            ex.message,              // Mensagem da exceção
            request.methodName,      // Método HTTP da requisição (GET, POST, etc.)
            request.path,            // Caminho da URI da requisição
            ex                       // A própria exceção para stack trace completo
        )
    }

    /**
     * Constrói e loga uma mensagem de erro detalhada para exceções tratadas globalmente,
     * sem um ID de erro específico.
     * @param ex A exceção ocorrida.
     * @param request A requisição HTTP associada à exceção.
     */
    fun buildLogErrorGlobalHandler(ex: Exception, request: HttpRequest<*>) {
        loggerGlobalHandler.error(
            """
            ERRO
            Tipo........: {}
            Mensagem....: {}
            Método......: {}
            Path........: {}
            """,
            ex.javaClass.simpleName,
            ex.message,
            request.methodName,
            request.path,
            ex
        )
    }

    /**
     * Constrói e loga uma mensagem de aviso detalhada para exceções tratadas globalmente.
     * @param ex A exceção ocorrida.
     * @param request A requisição HTTP associada à exceção.
     */
    fun buildLogWarnGlobalHandler(ex: Exception, request: HttpRequest<*>) {
        loggerGlobalHandler.warn(
            """
            WARN
            Tipo........: {}
            Mensagem....: {}
            Método......: {}
            Path........: {}
            """,
            ex.javaClass.simpleName,
            ex.message,
            request.methodName,
            request.path,
            ex
        )
    }

    /**
     * Constrói e loga uma mensagem de erro genérica para qualquer componente da aplicação.
     * @param ex A exceção ocorrida.
     * @param component A classe do componente onde o erro ocorreu, usada para o nome do logger.
     */
    fun <T> buildLogErrorGeneric(ex: Exception, component: Class<T>) {
        val logger = LoggerFactory.getLogger(component) // Cria um logger específico para o componente
        logger.error(
            """
            ERRO
            Tipo........: {}
            Mensagem....: {}
            """,
            ex.javaClass.simpleName,
            ex.message,
            ex
        )
    }

    /**
     * Constrói e loga uma mensagem de aviso genérica para qualquer componente da aplicação.
     * @param ex A exceção ocorrida.
     * @param component A classe do componente onde o aviso ocorreu, usada para o nome do logger.
     */
    fun <T> buildLogWarnGeneric(ex: Exception, component: Class<T>) {
        val logger = LoggerFactory.getLogger(component)
        logger.warn(
            """
            WARN
            Tipo........: {}
            Mensagem....: {}
            """,
            ex.javaClass.simpleName,
            ex.message,
            ex
        )
    }
}
