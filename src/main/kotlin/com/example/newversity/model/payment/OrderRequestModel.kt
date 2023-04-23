package com.example.newversity.model.payment

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class OrderRequestModel(
        var amount: Int? = null,
        var availabilityId: String? = null,
        var currency: String? = null,
        var userId: String? = null,
)