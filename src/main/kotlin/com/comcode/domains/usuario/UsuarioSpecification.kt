package com.comcode.domains.usuario

import jakarta.persistence.criteria.Root as JpaRoot
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Predicate as JpaPredicate
import com.comcode.utils.PredicateUtils
import io.micronaut.data.repository.jpa.criteria.PredicateSpecification

object UsuarioSpecification {
    fun build(nome: String?): PredicateSpecification<UsuarioModel> {
        return PredicateSpecification { root: JpaRoot<UsuarioModel>, builder: CriteriaBuilder ->
            val predicates = mutableListOf<JpaPredicate>()

            // Reaproveita o helper do PredicateUtils (não redeclare a função aqui)
            PredicateUtils.addStringLikeUnaccentPredicate(root, builder, predicates, nome, "nome")

            if (predicates.isEmpty()) null else builder.and(*predicates.toTypedArray())
        }
    }
}
