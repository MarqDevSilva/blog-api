package com.comcode.domains.technology

import com.comcode.domains.technology.dto.TechnologyDto
import jakarta.inject.Singleton
import com.comcode.templates.GenericServiceTemplate

@Singleton
open class TechnologyService(
    override val repository: TechnologyRepository,
    override val mapper: TechnologyMapper
) : GenericServiceTemplate<TechnologyModel, Long, TechnologyDto>(repository, repository, mapper) ,ITechnologyService  {
}