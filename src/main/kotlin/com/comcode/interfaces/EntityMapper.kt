package com.comcode.interfaces

import org.mapstruct.MappingTarget

/**
 * Contrato genérico para mapeamento entre DTO e Entity.
 *
 * @param D Tipo do DTO.
 * @param E Tipo da Entidade.
 */
interface EntityMapper<D, E> {

    fun toEntity(dto: D): E
    fun toDto(entity: E): D

    fun toEntity(dtoList: List<D>?): List<E> {
        return dtoList?.map { toEntity(it) } ?: emptyList()
    }

    fun toDto(entityList: List<E>?): List<D> {
        return entityList?.map { toDto(it) } ?: emptyList()
    }

    fun patchToEntity(dto: D, @MappingTarget entity: E): E

    fun toEntityParallel(dtoList: List<D>?): List<E> {
        return dtoList?.parallelStream()
            ?.map { toEntity(it) }
            ?.toList()
            ?: emptyList()
    }

    fun toDtoParallel(entityList: List<E>?): List<D> {
        return entityList?.parallelStream()
            ?.map { toDto(it) }
            ?.toList()
            ?: emptyList()
    }
}
