package com.example.newversity.model.student

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SessionCountModel (
    var totalSessionCount: Int? = null,
    var upcomingSessionCount: Int? = null,
    var previousSessionCount: Int? = null
)