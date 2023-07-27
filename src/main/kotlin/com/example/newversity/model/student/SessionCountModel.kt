package com.example.newversity.model.student

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
class SessionCountModel {
    var totalSessionCount: Int? = null
    var upcomingSessionCount: Int? = null
    var previousSessionCount: Int? = null
}