package com.comcode.domains.usuario

import com.comcode.domains.usuario.dto.LoginResponse
import com.comcode.domains.usuario.events.UsuarioSignUpEvent
import io.micronaut.context.event.ApplicationEventPublisher
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.HttpResponse
import com.comcode.templates.GenericControllerTemplate
import com.comcode.domains.usuario.dto.UsuarioDto
import io.micronaut.http.annotation.QueryValue
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.Sort
import io.micronaut.data.repository.jpa.criteria.PredicateSpecification
import io.micronaut.http.HttpRequest
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.handlers.LoginHandler
import io.micronaut.security.rules.SecurityRule

@Controller("/usuarios")
@Secured(SecurityRule.IS_ANONYMOUS)
class UsuarioController(
    override val service: IUsuarioService,
    private val loginHandler: LoginHandler<HttpRequest<*>, HttpResponse<LoginResponse>>,
    private val publisher: ApplicationEventPublisher<Any>
) : GenericControllerTemplate<UsuarioModel, Long, UsuarioDto>(service) {

    /**
     * Busca paginada de usuários com filtro opcional de login.
     * Exemplo: /usuarios/search?page=0&size=10&login=douglas
     */
    @Get("/search")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun searchPage(
        @QueryValue(defaultValue = "0") page: Int,
        @QueryValue(defaultValue = "10") size: Int,
        @QueryValue nome: String,
        @QueryValue email: String?
    ): HttpResponse<Page<UsuarioDto>> {
        val pageable = Pageable.from(page, size, Sort.of(Sort.Order.asc("nome")))
        val spec: PredicateSpecification<UsuarioModel> = UsuarioSpecification.build(nome)
        val pageResult: Page<UsuarioDto> = service.findAllBySpecification(spec, pageable)
        return HttpResponse.ok(pageResult)
    }

    @Post("/signup")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun signup(@Body dto: UsuarioDto, request: HttpRequest<*>): HttpResponse<*> {
        val authentication = service.signup(dto)
        publisher.publishEventAsync(UsuarioSignUpEvent(authentication))
        return loginHandler.loginSuccess(authentication, request)
    }
}