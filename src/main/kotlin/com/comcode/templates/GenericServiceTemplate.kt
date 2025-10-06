package com.comcode.templates

import io.micronaut.data.jpa.repository.JpaRepository
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.Page
import io.micronaut.data.repository.jpa.JpaSpecificationExecutor
import io.micronaut.data.repository.jpa.criteria.PredicateSpecification
import jakarta.transaction.Transactional
import com.comcode.interfaces.EntityMapper
import org.slf4j.LoggerFactory
import com.comcode.exceptions.NotFoundException

open class GenericServiceTemplate<T : Any, ID : Any, DTO>(
    protected open val repository: JpaRepository<T, ID>,
    protected open val specification: JpaSpecificationExecutor<T>,
    protected open val mapper: EntityMapper<DTO, T>
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    open fun save(entity: T): T {
        return repository.save(entity)
    }

    @Transactional
    open fun saveAll(entities: List<T>): List<T> {
        return repository.saveAll(entities).toList()
    }

    @Transactional
    open fun saveDTO(dto: DTO): DTO {
        return mapper.toDto(repository.save(mapper.toEntity(dto)))
    }

    @Transactional
    open fun saveAllDTO(listDTO: List<DTO>): List<DTO> {
        return mapper.toDto(repository.saveAll(mapper.toEntity(listDTO)))
    }

    @Transactional
    open fun update(entity: T): T {
        return repository.update(entity)
    }

    @Transactional
    open fun updateDTO(id: ID, dto: DTO): DTO {
        if (!existById(id)) throw NotFoundException("Entidade com id $id não encontrada")
        return mapper.toDto(repository.update(mapper.toEntity(dto)))
    }

    @Transactional
    open fun patchDTO(id: ID, dto: DTO): DTO {
        val entity = repository.findById(id).orElseThrow { NotFoundException("Entidade com id $id não encontrada") }
        val patchedEntity = mapper.patchToEntity(dto, entity)
        return mapper.toDto(repository.update(patchedEntity))
    }

    @Transactional
    open fun delete(id: ID) {
        if (!existById(id)) throw NotFoundException("Entidade com id $id não encontrada")
        repository.deleteById(id)
    }

    @Transactional
    open fun deleteAll(entities: List<T>) {
        repository.deleteAll(entities)
    }

    open fun findAll(): List<T> {
        return repository.findAll().toList()
    }

    open fun findAllDTO(): List<DTO> {
        return mapper.toDto(repository.findAll().toList())
    }

    open fun findById(id: ID): T {
        return repository.findById(id).orElseThrow { NotFoundException("Entidade com id $id não encontrada") }
    }

    open fun findByIdDTO(id: ID): DTO {
        val entity = repository.findById(id).orElseThrow { NotFoundException("Entidade com id $id não encontrada") }
        return mapper.toDto(entity)
    }

    open fun existById(id: ID): Boolean {
        return repository.existsById(id)
    }

    open fun findAllPageSorted(pageable: Pageable): Page<T> {
        return repository.findAll(pageable)
    }

    open fun findAllPageAsDTOSorted(pageable: Pageable): Page<DTO> {
        val page = repository.findAll(pageable)
        return page.map(mapper::toDto)
    }

    /**
    * Busca paginada a partir de uma PredicateSpecification (quando você só precisa montar WHERE).
    * Aceita null (retorna todos aplicando apenas pageable).
    */
    open fun findAllBySpecification(spec: PredicateSpecification<T>?, pageable: Pageable): Page<DTO> {
        val page = specification.findAll(spec, pageable)
        return page.map(mapper::toDto)
    }
}
