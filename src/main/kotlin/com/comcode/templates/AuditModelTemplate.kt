package com.comcode.templates

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import java.io.Serializable
import java.time.LocalDateTime
import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.DateUpdated

@MappedSuperclass
@JsonIgnoreProperties(
    value = ["createdAt", "updatedAt"],
    allowGetters = true
)
abstract class AuditModelTemplate : Serializable {

    companion object {
        private const val serialVersionUID: Long = 1L
    }

    /**
     * Timestamp de criação.
     * Micronaut Data popula automaticamente campos anotados com @DateCreated
     * quando você usa repositórios do Micronaut Data.
     */
    @DateCreated
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    open var createdAt: LocalDateTime? = null

    /**
     * Timestamp de atualização.
     * Micronaut Data popula automaticamente campos anotados com @DateUpdated.
     */
    @DateUpdated
    @Column(name = "updated_at", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    open var updatedAt: LocalDateTime? = null

    /**
     * @todo: adicionar createdBy / lastModifiedBy quando houver AuditorAware equivalente
     * no Micronaut (p.ex. interceptor que injeta o usuário atual).
     */
}