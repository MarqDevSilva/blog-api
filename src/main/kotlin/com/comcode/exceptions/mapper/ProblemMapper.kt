package com.comcode.exceptions.mapper

import com.comcode.exceptions.dto.ProblemDto
import jakarta.inject.Singleton
import org.zalando.problem.Problem
import java.net.URI

/**
 * Mapper manual para conversão entre Problem (Zalando) e ProblemDto.
 * Implementação manual necessária porque Problem é uma interface abstrata 
 * e o MapStruct não consegue gerar implementação automática.
 */
@Singleton
class ProblemMapper {

    /**
     * Converte um Problem (Zalando) para ProblemDto.
     */
    fun toDto(problem: Problem): ProblemDto {
        return ProblemDto(
            type = problem.type?.toString() ?: "about:blank",
            title = problem.title ?: "Erro",
            status = problem.status?.toString() ?: "500",
            detail = problem.detail ?: "Erro interno do servidor",
            instance = problem.instance?.toString() ?: "",
            parameters = problem.parameters
        )
    }
}