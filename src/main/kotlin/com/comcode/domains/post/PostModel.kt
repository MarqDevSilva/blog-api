package com.comcode.domains.post

import com.comcode.domains.collection.CollectionModel
import com.comcode.domains.technology.TechnologyModel
import com.comcode.enums.StatusPost
import io.micronaut.data.annotation.NamingStrategy
import io.micronaut.data.model.naming.NamingStrategies
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Column
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDate
import com.comcode.domains.usuario.UsuarioModel

@Entity
@Table(name = "TB_POST")
@NamingStrategy(NamingStrategies.UnderScoreSeparatedUpperCase::class)
data class PostModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne()
    @JoinColumn(nullable = false)
    val author: UsuarioModel,

    @ManyToOne()
    @JoinColumn(nullable = true)
    val collection: CollectionModel,

    @ManyToMany()
    @JoinTable(
        name = "post_technology",
        joinColumns = [JoinColumn(name = "post_id")],
        inverseJoinColumns = [JoinColumn(name = "technology_id")]
    )
    val technologies: List<TechnologyModel>,

    @Column(nullable = false)
    val slug: String,

    @Column(nullable = false)
    val summary: Short,

    @Column(nullable = false)
    val content: String,

    @Column(nullable = false)
    val contentHtml: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: StatusPost,

    @Column(nullable = false)
    val publishedAt: LocalDate,

    @Column(nullable = false)
    val metaTitle: String,

    @Column(nullable = false)
    val metaDescription: String,

    @Column()
    val canonicalUrl: String?,

    @Column()
    val viewsCount: Long = 0L,

    @Column()
    val likesCount: Long = 0L
){}
