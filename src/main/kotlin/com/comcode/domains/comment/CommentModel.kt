package com.comcode.domains.comment

import com.comcode.domains.post.PostModel
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import io.micronaut.data.annotation.NamingStrategy
import io.micronaut.data.model.naming.NamingStrategies
import jakarta.persistence.*
import com.comcode.domains.usuario.UsuarioModel

@Entity
@Table(name = "TB_COMMENT")
@NamingStrategy(NamingStrategies.UnderScoreSeparatedUpperCase::class)
data class CommentModel (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val content: String,

    @ManyToOne()
    @JoinColumn(nullable = false)
    val author: UsuarioModel,

    @ManyToOne()
    @JoinColumn(nullable = false)
    val post: PostModel,

    @ManyToOne()
    @JoinColumn()
    @JsonBackReference()
    val parent: CommentModel? = null,

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    @OrderBy(" id ASC")
    @JsonManagedReference()
    val replies: List<CommentModel> = emptyList()
)