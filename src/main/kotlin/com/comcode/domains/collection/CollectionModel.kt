package com.comcode.domains.collection

import com.comcode.domains.post.PostModel
import com.comcode.domains.usuario.UsuarioModel
import io.micronaut.data.annotation.NamingStrategy
import io.micronaut.data.model.naming.NamingStrategies
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "TB_COLLECTION")
@NamingStrategy(NamingStrategies.UnderScoreSeparatedUpperCase::class)
data class CollectionModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne()
    @JoinColumn(nullable = false)
    val author: UsuarioModel,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val slug: String,

    @OneToMany(mappedBy = "collection")
    val posts: List<PostModel> = emptyList()

)