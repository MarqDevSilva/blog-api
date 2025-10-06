package com.comcode.domains.technology

import com.comcode.domains.technology.dto.TechnologyDto
import io.micronaut.http.annotation.Controller
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import com.comcode.templates.GenericControllerTemplate

@Controller("/technologies")
@Secured(SecurityRule.IS_ANONYMOUS)
class TechnologyController(
    override  val service: ITechnologyService
): GenericControllerTemplate<TechnologyModel, Long, TechnologyDto>(service) {}