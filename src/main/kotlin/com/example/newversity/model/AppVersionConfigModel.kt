package com.example.newversity.model

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming


@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class AppVersionConfigModel(
        var mandatory: Boolean? = null,
        var version: String? = null,
)