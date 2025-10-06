package com.comcode.domains.technology

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import io.micronaut.data.repository.jpa.JpaSpecificationExecutor

@Repository
interface TechnologyRepository : JpaRepository<TechnologyModel, Long>, JpaSpecificationExecutor<TechnologyModel> {}