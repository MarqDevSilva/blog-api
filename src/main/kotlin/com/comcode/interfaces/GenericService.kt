package com.comcode.interfaces

import io.micronaut.data.model.Pageable
import io.micronaut.data.model.Page
import io.micronaut.data.repository.jpa.criteria.PredicateSpecification

/**
 * Interface genérica de serviço para operações CRUD
 * @param T Tipo da entidade
 * @param ID Tipo do ID da entidade
 * @param DTO Tipo do DTO
 */
interface GenericService<T : Any, ID : Any, DTO> {

    fun saveDTO(dto: DTO): DTO

    fun saveAllDTO(dtos: List<DTO>): List<DTO>

    fun updateDTO(id: ID, dto: DTO): DTO

    fun patchDTO(id: ID, dto: DTO): DTO

    fun delete(id: ID)

    fun deleteAll(entities: List<T>)

    fun findAllDTO(): List<DTO>

    fun findAllPageAsDTOSorted(pageable: Pageable): Page<DTO>

    fun findByIdDTO(id: ID): DTO

    fun findAllBySpecification(spec: PredicateSpecification<T>?, pageable: Pageable): Page<DTO>
}
