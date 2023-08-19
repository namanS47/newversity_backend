package com.example.newversity.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class NotificationDetailsModel (
        var userId: String? = null,
        var userIds: List<String>? = null,
        var title: String? = null,
        var body: String? = null,
        var data: HashMap<String, String> = hashMapOf(),
)