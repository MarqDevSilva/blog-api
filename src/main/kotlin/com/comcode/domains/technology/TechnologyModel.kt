package com.comcode.domains.technology

import com.comcode.domains.media.MediaModel
import com.comcode.enums.TypeTech
import io.micronaut.data.annotation.NamingStrategy
import io.micronaut.data.model.naming.NamingStrategies
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "TB_TECHNOLOGY")
@NamingStrategy(NamingStrategies.UnderScoreSeparatedUpperCase::class)
data class TechnologyModel (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val typeTech: TypeTech,

    @OneToOne()
    @JoinColumn(nullable = true)
    val icon: MediaModel?
){}