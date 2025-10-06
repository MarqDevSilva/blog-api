package com.comcode.domains.technology.dto

import com.comcode.domains.media.dto.MediaDto
import com.comcode.enums.TypeTech

data class TechnologyDto(

    val id: Long?,
    val name: String,
    val typeTech: TypeTech,
    val icon: MediaDto?
)
