package com.comcode.exceptions.handlers

import com.comcode.exceptions.AlreadyExistsException
import com.comcode.exceptions.EmailSendException
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Error
import jakarta.persistence.OptimisticLockException
import jakarta.validation.ConstraintViolation
import jakarta.validation.ConstraintViolationException
import org.zalando.problem.Problem
import java.net.ConnectException
import java.net.SocketException
import java.nio.file.AccessDeniedException
import java.util.UUID
import java.util.concurrent.TimeoutException
import java.util.stream.Collectors
import io.micronaut.data.exceptions.DataAccessException
import java.lang.IllegalArgumentException
import com.comcode.utils.LoggerUtils
import com.comcode.utils.ProblemDetailUtils
import com.comcode.exceptions.NotFoundException
import com.comcode.exceptions.dto.ProblemDto
import com.comcode.exceptions.mapper.ProblemMapper
import io.micronaut.http.annotation.Controller
import io.micronaut.security.authentication.AuthenticationException
import io.micronaut.security.authentication.AuthorizationException

/**
 * @Details Padrão RFC 7807 nas mensagens de erro (Problem JSON)
 * @Details Manipulador de Exceções Global para aplicações Micronaut,
 * capturando e tratando diversas exceções para retornar respostas padronizadas.
 * @author Bruno Omar Menezes Silveira
 * @Date 08/07/2025
 */
@Controller
class GlobalExceptionHandler (
    private val mapper: ProblemMapper
){

    /**
     * Manipula exceções de violação de integridade de dados (e.g., chave duplicada).
     * Mapeia para HttpStatus.CONFLICT (409).
     * @param ex A exceção DataAccessException (Micronaut Data).
     * @param request A requisição HTTP.
     * @return Uma resposta HTTP contendo um Problem JSON.
     */
    @Error(exception = DataAccessException::class, global = true)
    fun handleDataIntegrityViolation(ex: DataAccessException, request: HttpRequest<*>): HttpResponse<ProblemDto> {
        LoggerUtils.buildLogWarnGlobalHandler(ex, request)
        val problem = ProblemDetailUtils.buildProblem(
            HttpStatus.CONFLICT,
            "Violacao de Integridade de Dados",
            ex.message ?: "Ocorreu uma violação de integridade de dados.",
            request
        )
        return HttpResponse.status<Problem>(HttpStatus.CONFLICT).body(mapper.toDto(problem))
    }

    /**
     * Manipula exceções de bloqueio otimista (concorrência de dados).
     * Mapeia para HttpStatus.CONFLICT (409).
     * @param ex A exceção OptimisticLockException (Jakarta Persistence).
     * @param request A requisição HTTP.
     * @return Uma resposta HTTP contendo um Problem JSON.
     */
    @Error(exception = OptimisticLockException::class, global = true)
    fun handleOptimisticLock(ex: OptimisticLockException, request: HttpRequest<*>): HttpResponse<ProblemDto> {
        LoggerUtils.buildLogWarnGlobalHandler(ex, request)
        val problem = ProblemDetailUtils.buildProblem(
            HttpStatus.CONFLICT,
            "Conflito de Concorrencia de Dados",
            ex.message ?: "Ocorreu um conflito de concorrência otimista.",
            request
        )
        return HttpResponse.status<Problem>(HttpStatus.CONFLICT).body(mapper.toDto(problem))
    }

    /**
     * Manipula exceções de envio de e-mail.
     * Mapeia para HttpStatus.INTERNAL_SERVER_ERROR (500).
     * @param ex A exceção EmailSendException.
     * @param request A requisição HTTP.
     * @return Uma resposta HTTP contendo um Problem JSON.
     */
    @Error(exception = EmailSendException::class, global = true)
    fun handleEmailSend(ex: EmailSendException, request: HttpRequest<*>): HttpResponse<ProblemDto> {
        LoggerUtils.buildLogWarnGlobalHandler(ex, request)
        val problem = ProblemDetailUtils.buildProblem(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Erro ao enviar e-mail",
            ex.message ?: "Ocorreu um erro inesperado ao tentar enviar o e-mail.",
            request
        )
        return HttpResponse.status<Problem>(HttpStatus.INTERNAL_SERVER_ERROR).body(mapper.toDto(problem))
    }

    /**
     * Manipula exceções de recurso não encontrado.
     * Mapeia para HttpStatus.NOT_FOUND (404).
     * @param ex A exceção NotFoundException (Micronaut HTTP).
     * @param request A requisição HTTP.
     * @return Uma resposta HTTP contendo um Problem JSON.
     */
    @Error(exception = NotFoundException::class, global = true)
    fun handleNotFoundException(ex: NotFoundException, request: HttpRequest<*>): HttpResponse<ProblemDto> {
        LoggerUtils.buildLogWarnGlobalHandler(ex, request)
        val problem = ProblemDetailUtils.buildProblem(
            HttpStatus.NOT_FOUND,
            "Recurso Não Encontrado",
            ex.message ?: "O recurso solicitado não foi encontrado.",
            request
        )
        return HttpResponse.status<Problem>(HttpStatus.NOT_FOUND).body(mapper.toDto(problem))
    }

    /**
     * Manipula exceções de validação de constraints.
     * Mapeia para HttpStatus.BAD_REQUEST (400).
     * Coleta todos os erros de validação em um mapa.
     * @param ex A exceção ConstraintViolationException (Jakarta Validation).
     * @param request A requisição HTTP.
     * @return Uma resposta HTTP contendo um Problem JSON com os erros de validação.
     */
    @Error(exception = ConstraintViolationException::class, global = true)
    fun handleConstraintViolation(ex: ConstraintViolationException, request: HttpRequest<*>): HttpResponse<ProblemDto> {
        LoggerUtils.buildLogWarnGlobalHandler(ex, request)
        val validationErrors = ex.constraintViolations.stream()
            .collect(Collectors.toMap(
                { violation: ConstraintViolation<*> -> violation.propertyPath.toString() }, // Caminho da propriedade violada
                { violation: ConstraintViolation<*> -> violation.message }                   // Mensagem de erro da violação
            ))

        val errorDetail = "Erro de validação em ${validationErrors.size} campo(s)"

        val problem = ProblemDetailUtils.buildProblem(
            HttpStatus.BAD_REQUEST,
            "Erro de Validação de Dados",
            errorDetail,
            request,
            validationErrors // Adiciona o mapa de erros de validação ao Problem
        )
        return HttpResponse.status<Problem>(HttpStatus.BAD_REQUEST).body(mapper.toDto(problem))
    }

    /**
     * Manipula exceções de acesso negado.
     * Mapeia para HttpStatus.FORBIDDEN (403).
     * @param ex A exceção AccessDeniedException (Java NIO File).
     * @param request A requisição HTTP.
     * @return Uma resposta HTTP contendo um Problem JSON.
     */
    @Error(exception = AccessDeniedException::class, global = true)
    fun handleAccessDenied(ex: AccessDeniedException, request: HttpRequest<*>): HttpResponse<ProblemDto> {
        LoggerUtils.buildLogWarnGlobalHandler(ex, request)
        val problem = ProblemDetailUtils.buildProblem(
            HttpStatus.FORBIDDEN,
            "Acesso ao Recurso Não Autorizado",
            ex.message ?: "Você não tem permissão para acessar este recurso.",
            request
        )
        return HttpResponse.status<Problem>(HttpStatus.FORBIDDEN).body(mapper.toDto(problem))
    }

    /**
     * Manipula exceções de argumento ilegal (e.g., ID inválido em um parâmetro).
     * Mapeia para HttpStatus.BAD_REQUEST (400).
     * @param ex A exceção IllegalArgumentException (Java Lang).
     * @param request A requisição HTTP.
     * @return Uma resposta HTTP contendo um Problem JSON.
     */
    @Error(exception = IllegalArgumentException::class, global = true)
    fun handleIllegalArgumentException(ex: IllegalArgumentException, request: HttpRequest<*>): HttpResponse<ProblemDto> {
        LoggerUtils.buildLogWarnGlobalHandler(ex, request)
        val problem = ProblemDetailUtils.buildProblem(
            HttpStatus.BAD_REQUEST,
            "ID da Requisição Inválido",
            ex.message ?: "O ID fornecido na requisição é inválido.",
            request
        )
        return HttpResponse.status<Problem>(HttpStatus.BAD_REQUEST).body(mapper.toDto(problem))
    }

    /**
     * Manipula exceções de infraestrutura como falhas de conexão ou socket.
     * Mapeia para HttpStatus.SERVICE_UNAVAILABLE (503).
     * @param ex A exceção (ConnectException ou SocketException).
     * @param request A requisição HTTP.
     * @return Uma resposta HTTP contendo um Problem JSON.
     */
    @Error(exception = ConnectException::class, global = true)
    fun handleConnectExceptions(ex: Exception, request: HttpRequest<*>): HttpResponse<ProblemDto> {
        LoggerUtils.buildLogErrorGlobalHandler(ex, request)
        val problem = ProblemDetailUtils.buildProblem(
            HttpStatus.SERVICE_UNAVAILABLE,
            "Falha na Comunicação com Dependência",
            ex.message ?: "Falha ao conectar com um serviço dependente.",
            request
        )
        return HttpResponse.status<Problem>(HttpStatus.SERVICE_UNAVAILABLE).body(mapper.toDto(problem))
    }

    @Error(exception = SocketException::class, global = true)
    fun handleSocketExceptions(ex: Exception, request: HttpRequest<*>): HttpResponse<ProblemDto> {
        LoggerUtils.buildLogErrorGlobalHandler(ex, request)
        val problem = ProblemDetailUtils.buildProblem(
            HttpStatus.SERVICE_UNAVAILABLE,
            "Falha na Comunicação com Dependência",
            ex.message ?: "Falha ao conectar com um serviço dependente.",
            request
        )
        return HttpResponse.status<Problem>(HttpStatus.SERVICE_UNAVAILABLE).body(mapper.toDto(problem))
    }

    /**
     * Manipula exceções de tempo limite de operação.
     * Mapeia para HttpStatus.SERVICE_UNAVAILABLE (503).
     * @param ex A exceção TimeoutException (Java Util Concurrent).
     * @param request A requisição HTTP.
     * @return Uma resposta HTTP contendo um Problem JSON.
     */
    @Error(exception = TimeoutException::class, global = true)
    fun handleTimeoutException(ex: TimeoutException, request: HttpRequest<*>): HttpResponse<ProblemDto> {
        LoggerUtils.buildLogErrorGlobalHandler(ex, request)
        val problem = ProblemDetailUtils.buildProblem(
            HttpStatus.SERVICE_UNAVAILABLE,
            "Tempo Limite de Operação Excedido",
            ex.message ?: "A operação excedeu o tempo limite.",
            request
        )
        return HttpResponse.status<Problem>(HttpStatus.SERVICE_UNAVAILABLE).body(mapper.toDto(problem))
    }

    /**
     * Manipula exceção de senha inválida.
     * Mapeia para HttpStatus.UNAUTHORIZED (401).
     */
    @Error(exception = AuthenticationException::class, global = true)
    fun handleAuthenticationException(ex: AuthenticationException, request: HttpRequest<*>): HttpResponse<ProblemDto> {
        LoggerUtils.buildLogWarnGlobalHandler(ex, request)
        val problem = ProblemDetailUtils.buildProblem(
            HttpStatus.FORBIDDEN,
            "Falha na Autenticação",
            ex.message ?: "Usuário ou senha inválidos",
            request
        )
        return HttpResponse.status<Problem>(HttpStatus.FORBIDDEN).body(mapper.toDto(problem))
    }

    @Error(exception = AlreadyExistsException::class, global = true)
    fun handleAlreadyExistsException(ex: AlreadyExistsException, request: HttpRequest<*>): HttpResponse<ProblemDto> {
        LoggerUtils.buildLogWarnGlobalHandler(ex, request)
        val problem = ProblemDetailUtils.buildProblem(
            HttpStatus.CONFLICT,
            "Recurso Já Existe",
            ex.message ?: "Não é possível criar o recurso pois ele já existe.",
            request
        )
        return HttpResponse.status<Problem>(HttpStatus.CONFLICT).body(mapper.toDto(problem))
    }

    @Error(exception = AuthorizationException::class, global = true)
    fun handleAuthorizationException(ex: AuthorizationException, request: HttpRequest<*>): HttpResponse<ProblemDto> {
        LoggerUtils.buildLogWarnGlobalHandler(ex, request)
        val problem = ProblemDetailUtils.buildProblem(
            HttpStatus.UNAUTHORIZED,
            "Acesso Negado",
            ex.message ?: "Você não tem permissão para acessar este recurso.",
            request
        )
        return HttpResponse.status<Problem>(HttpStatus.UNAUTHORIZED).body(mapper.toDto(problem))
    }

    /**
     * Manipulador genérico para qualquer exceção não tratada especificamente pelos outros handlers.
     * Mapeia para HttpStatus.INTERNAL_SERVER_ERROR (500).
     * Gera um ID de erro único para cada ocorrência inesperada.
     * @param ex A exceção genérica.
     * @param request A requisição HTTP.
     * @return Uma resposta HTTP contendo um Problem JSON com ID de erro.
     */
    @Error(exception = Exception::class, global = true)
    @Requires(notEnv = ["test"]) // Evita conflitos com outros handlers em ambientes de teste, se houver.
    fun handleUnhandledExceptions(ex: Exception, request: HttpRequest<*>): HttpResponse<ProblemDto> {
        val errorId = UUID.randomUUID().toString() // Gera um UUID para rastreamento
        LoggerUtils.buildLogErrorGlobalHandler(ex, request, errorId)
        val problem = ProblemDetailUtils.buildProblem(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Falha Inesperada no Sistema",
            ex.message ?: "Ocorreu um erro interno inesperado no servidor.",
            request,
            errorId
        )
        return HttpResponse.status<Problem>(HttpStatus.INTERNAL_SERVER_ERROR).body(mapper.toDto(problem))
    }
}
