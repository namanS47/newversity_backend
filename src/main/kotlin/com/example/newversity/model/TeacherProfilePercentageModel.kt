package com.example.newversity.model

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
class TeacherProfilePercentageModel {
    var completePercentage: Int? = null
    var reason: String? = null
    var suggestion: String? = null
}