package com.comcode.domains.usuario

import com.comcode.enums.Provider
import com.comcode.enums.Role
import com.comcode.templates.AuditModelTemplate
import io.micronaut.data.annotation.NamingStrategy
import io.micronaut.data.model.naming.NamingStrategies
import jakarta.persistence.*
import org.mindrot.jbcrypt.BCrypt

@Entity
@Table(name = "TB_USER")
@NamingStrategy(NamingStrategies.UnderScoreSeparatedUpperCase::class)
data class UsuarioModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val nome: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false, unique = true)
    val slug: String,

    @Column(nullable = false)
    val bio: String,

    @Column()
    var password: String,

    @Column()
    val verified: Boolean = false,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var provider: Provider?,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var role: Role?
) : AuditModelTemplate(){

    @PrePersist
    fun prePersist() {
        if(!password.matches("^\\$2[aby]\\$\\d{2}\\$.{53}$".toRegex())) {
            password = BCrypt.hashpw(password, BCrypt.gensalt())
        }

        if(provider == null) {
            provider = Provider.DEFAULT
        }

        if(role == null) {
            role = Role.USUARIO
        }
    }

    @PreUpdate
    fun preUpdate() {
        if(!password.matches("^\\$2[aby]\\$\\d{2}\\$.{53}$".toRegex())) {
            password = BCrypt.hashpw(password, BCrypt.gensalt())
        }
    }


}