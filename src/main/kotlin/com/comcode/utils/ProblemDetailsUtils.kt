package com.comcode.utils

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import org.zalando.problem.Problem
import org.zalando.problem.Status
import java.net.URI
import java.time.Instant

/**
 * Utilitário para construir objetos Problem (RFC 7807) para respostas de erro padronizadas.
 * Utiliza a biblioteca Micronaut-Problem-Json (Zalando Problem).
 */
object ProblemDetailUtils {

    /**
     * Constrói um objeto Problem básico com status, título, detalhes e informações da requisição.
     * @param status O status HTTP do problema.
     * @param title O título do problema (curto, legível por humanos).
     * @param detail Detalhes específicos do problema (legível por humanos, pode incluir mais contexto).
     * @param request A requisição HTTP associada ao problema.
     * @return Um objeto Problem configurado.
     */
    fun buildProblem(status: HttpStatus, title: String, detail: String, request: HttpRequest<*>): Problem {
        // Converte o HttpStatus do Micronaut para o Status do Zalando Problem
        val statusZalando = Status.valueOf(status.code)
        return Problem.builder()
            .withStatus(statusZalando)       // Status HTTP do problema
            .withTitle(title)                // Título do problema
            .withDetail(detail)              // Detalhes da mensagem
            .withInstance(URI.create(request.path)) // URI da instância do problema (caminho da requisição)
            .with("path", request.path)      // Propriedade customizada: caminho da requisição
            .with("method", request.methodName) // Propriedade customizada: método HTTP da requisição
            .with("timestamp", Instant.now().toString()) // Propriedade customizada: timestamp do erro
            .build()
    }

    /**
     * Constrói um objeto Problem com um ID de erro adicional para rastreamento.
     * @param status O status HTTP do problema.
     * @param title O título do problema.
     * @param detail Detalhes específicos do problema.
     * @param request A requisição HTTP associada.
     * @param erroId Um ID único para o erro, facilitando o rastreamento em logs.
     * @return Um objeto Problem configurado com o ID de erro.
     */
    fun buildProblem(status: HttpStatus, title: String, detail: String, request: HttpRequest<*>, erroId: String): Problem {
        // Cria um Problem base e adiciona a propriedade "erroId"
        val baseProblem = buildProblem(status, title, detail, request)
        return Problem.builder()
            .withStatus(baseProblem.status)
            .withTitle(baseProblem.title)
            .withDetail(baseProblem.detail)
            .withInstance(baseProblem.instance)
            .with("path", baseProblem.parameters["path"])
            .with("method", baseProblem.parameters["method"])
            .with("timestamp", baseProblem.parameters["timestamp"])
            .with("erroId", erroId)
            .build()
    }

    /**
     * Constrói um objeto Problem com um mapa de erros de validação adicionais.
     * @param status O status HTTP do problema.
     * @param title O título do problema.
     * @param detail Detalhes específicos do problema.
     * @param request A requisição HTTP associada.
     * @param errors Um mapa de campos e suas respectivas mensagens de erro de validação.
     * @return Um objeto Problem configurado com os erros de validação.
     */
    fun buildProblem(status: HttpStatus, title: String, detail: String, request: HttpRequest<*>, errors: Map<String, String>): Problem {
        // Cria um Problem base e adiciona a propriedade "erros" (mapa de erros de validação)
        val baseProblem = buildProblem(status, title, detail, request)
        return Problem.builder()
            .withStatus(baseProblem.status)
            .withTitle(baseProblem.title)
            .withDetail(baseProblem.detail)
            .withInstance(baseProblem.instance)
            .with("path", baseProblem.parameters["path"])
            .with("method", baseProblem.parameters["method"])
            .with("timestamp", baseProblem.parameters["timestamp"])
            .with("erros", errors)
            .build()
    }
}