package com.comcode.utils

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Expression
import jakarta.persistence.criteria.Predicate as JpaPredicate
import jakarta.persistence.criteria.Root as JpaRoot
import java.text.Normalizer
import kotlin.text.lowercase

object PredicateUtils {

    /**
     * Adiciona um predicado de busca "like" ignorando acentos para uma lista de palavras.
     *
     * @param root A raiz da consulta.
     * @param builder O builder de critérios.
     * @param predicates A lista de predicados.
     * @param param A string de busca.
     * @param name O nome do campo.
     */
    fun <T> addStringLikeUnaccentPredicate(
        root: JpaRoot<T>,
        builder: CriteriaBuilder,
        predicates: MutableList<JpaPredicate>,
        param: String?,
        name: String
    ) {
        if (!param.isNullOrBlank()) {
            val normalizedParam = Normalizer.normalize(param.lowercase(), Normalizer.Form.NFD)
                .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            
            val words = normalizedParam.split("\\s+".toRegex())
                .filter { it.isNotBlank() }
            
            words.forEach { palavra ->
                predicates.add(
                    builder.like(
                        builder.lower(builder.function("unaccent", String::class.java, root.get<String>(name))),
                        "%$palavra%"
                    )
                )
            }
        }
    }

    /**
     * Adiciona um predicado de similaridade de palavras usando a função word_similarity.
     *
     * @param root A raiz da consulta.
     * @param builder O builder de critérios.
     * @param predicates A lista de predicados.
     * @param param A string de busca.
     * @param name O nome do campo.
     * @param threshold O limite de similaridade.
     */
    fun <T> addStringLikeUnaccentWordSimilarityPredicate(
        root: JpaRoot<T>,
        builder: CriteriaBuilder,
        predicates: MutableList<JpaPredicate>,
        param: String?,
        name: String,
        threshold: Double
    ) {
        if (!param.isNullOrBlank()) {
            val unaccentParam = Normalizer.normalize(param, Normalizer.Form.NFD)
                .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")

            val unaccentColumn: Expression<String> = builder.function(
                "unaccent_immutable",
                String::class.java,
                root.get<String>(name)
            )

            val similarity: Expression<Double> = builder.function(
                "word_similarity",
                Double::class.java,
                unaccentColumn,
                builder.literal(unaccentParam)
            )

            val similarityPredicate = builder.and(
                builder.isNotNull(root.get<String>(name)),
                builder.greaterThanOrEqualTo(similarity, threshold)
            )

            predicates.add(similarityPredicate)
        }
    }

    /**
     * Adiciona um predicado de similaridade de palavras com o threshold padrão.
     */
    fun <T> addStringLikeUnaccentWordSimilarityPredicate(
        root: JpaRoot<T>,
        builder: CriteriaBuilder,
        predicates: MutableList<JpaPredicate>,
        param: String?,
        name: String
    ) {
        val SIMILARITY_THRESHOLD = 0.6
        addStringLikeUnaccentWordSimilarityPredicate(root, builder, predicates, param, name, SIMILARITY_THRESHOLD)
    }

    /**
     * Adiciona um predicado de igualdade para um campo.
     */
    fun <T, P> addStringEqualsPredicate(
        root: JpaRoot<T>,
        builder: CriteriaBuilder,
        predicates: MutableList<JpaPredicate>,
        param: P?,
        name: String
    ) {
        param?.let { predicates.add(builder.equal(root.get<P>(name), it)) }
    }

    /**
     * Adiciona um predicado de igualdade para o ID de uma entidade relacionada.
     */
    fun <T> addEntityIdEqualsPredicate(
        root: JpaRoot<T>,
        builder: CriteriaBuilder,
        predicates: MutableList<JpaPredicate>,
        id: Long?,
        entityName: String
    ) {
        id?.let { predicates.add(builder.equal(root.get<Any>(entityName).get<Long>("id"), it)) }
    }

    /**
     * Adiciona um predicado de igualdade para uma lista de valores.
     */
    fun <T, P> addStringEqualsPredicate(
        root: JpaRoot<T>,
        builder: CriteriaBuilder,
        predicates: MutableList<JpaPredicate>,
        param: List<P>?,
        name: String
    ) {
        param?.forEach { item ->
            item?.let { predicates.add(builder.equal(root.get<P>(name), it)) }
        }
    }
}