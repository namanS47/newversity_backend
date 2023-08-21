package com.example.newversity.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AppVersionConfigModel(
        var mandatory: Boolean? = null,
        var version: String? = null,
)
