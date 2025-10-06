package com.comcode.domains.usuario

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import io.micronaut.data.repository.jpa.JpaSpecificationExecutor

@Repository
interface UsuarioRepository : JpaRepository<UsuarioModel, Long>, JpaSpecificationExecutor<UsuarioModel> {
    fun findByEmail(email: String): UsuarioModel?
    fun existsByEmail(email: String): Boolean
}