package com.comcode.config

import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.TimeZone
import jakarta.annotation.PostConstruct

@Factory
class DateTimeFormat {

    private val zoneId: ZoneId = ZoneId.of("America/Sao_Paulo")

    @PostConstruct
    fun init() {
        // Define o timezone default da JVM
        TimeZone.setDefault(TimeZone.getTimeZone(zoneId))
    }

    @Singleton
    fun defaultZoneId(): ZoneId = zoneId

    @Singleton
    fun dateTimeFormatter(): DateTimeFormatter =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(zoneId)
}
