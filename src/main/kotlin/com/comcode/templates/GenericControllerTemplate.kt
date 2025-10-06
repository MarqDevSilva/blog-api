package com.comcode.templates

import com.comcode.interfaces.GenericService
import org.slf4j.LoggerFactory
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.Sort
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import jakarta.validation.Valid

/**
 * Controller genérico para Micronaut + Kotlin
 * @param T Tipo da Entidade
 * @param D Tipo do DTO
 * @param ID Tipo do ID
 */
abstract class GenericControllerTemplate<T : Any, ID : Any, DTO>(
    protected open val service: GenericService<T, ID, DTO>
) {

    // GET paginado
    @Get("/page")
    open fun findAllPagesSorted(
        @QueryValue(defaultValue = "0") page: Int,
        @QueryValue(defaultValue = "10") size: Int,
    ): HttpResponse<Page<DTO>> {
        val pageable = Pageable.from(page, size, Sort.of(Sort.Order.asc("id")))
        return HttpResponse.ok(service.findAllPageAsDTOSorted(pageable))
    }

    // GET all
    @Get("/")
    open fun findAll(): HttpResponse<List<DTO>> {
        return HttpResponse.ok(service.findAllDTO())
    }

    // GET por ID
    @Get("/{id}")
    open fun findById(@PathVariable id: ID): HttpResponse<DTO> {
        return HttpResponse.ok(service.findByIdDTO(id))
    }

    // POST
    @Post("/")
    open fun save(@Body @Valid dto: DTO): HttpResponse<DTO> {
        return HttpResponse.created(service.saveDTO(dto))
    }

    // PUT
    @Put("/{id}")
    open fun update(@PathVariable id: ID, @Body @Valid dto: DTO): HttpResponse<DTO> {
        return HttpResponse.ok(service.updateDTO(id, dto))
    }

    @Patch("/{id}")
    open fun patch(@PathVariable id: ID, @Body dto: DTO): HttpResponse<DTO> {
        return HttpResponse.ok(service.patchDTO(id, dto))
    }

    // DELETE
    @Delete("/{id}")
    open fun delete(@PathVariable id: ID): HttpResponse<Any> {
        service.delete(id)
        return HttpResponse.noContent()
    }
}
