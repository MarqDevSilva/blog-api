package com.comcode.domains.technology

import com.comcode.domains.technology.dto.TechnologyDto
import org.comcode.config.MapperConfigBase
import com.comcode.interfaces.EntityMapper
import org.mapstruct.Mapper

@Mapper(config = MapperConfigBase::class)
interface TechnologyMapper : EntityMapper<TechnologyDto, TechnologyModel>{}