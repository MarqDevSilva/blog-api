package org.comcode.config

import org.mapstruct.MapperConfig
import org.mapstruct.ReportingPolicy
import org.mapstruct.NullValueMappingStrategy
import org.mapstruct.NullValueCheckStrategy
import org.mapstruct.InjectionStrategy
import org.mapstruct.NullValuePropertyMappingStrategy
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import org.mapstruct.Builder


/**
 * Configuração base para todos os mappers MapStruct
 */
@MapperConfig(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = "jsr330",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    builder = Builder(disableBuilder = true),
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
interface MapperConfigBase {}
