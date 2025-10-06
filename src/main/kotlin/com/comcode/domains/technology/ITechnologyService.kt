package com.comcode.domains.technology

import com.comcode.domains.technology.dto.TechnologyDto
import com.comcode.interfaces.GenericService

interface ITechnologyService : GenericService<TechnologyModel, Long, TechnologyDto> {}