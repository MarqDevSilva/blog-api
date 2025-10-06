package com.comcode.domains.media

import io.micronaut.data.annotation.NamingStrategy
import io.micronaut.data.model.naming.NamingStrategies
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "TB_MEDIA")
@NamingStrategy(NamingStrategies.UnderScoreSeparatedUpperCase::class)
data class MediaModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val url: String,

    @Column(nullable = false)
    val fileName: String,

    @Column(nullable = false)
    val size: Long,

    @Column(nullable = false)
    val mimeType: String
)
