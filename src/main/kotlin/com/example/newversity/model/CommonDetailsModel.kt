package com.example.newversity.model

import com.example.newversity.entity.common.CommonDetails
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class CommonDetailsModel (
        var firebaseUserId: String? = null,
        var fcmToken: String?
)

object CommonDetailConverter {
    fun toEntity(commonDetailsModel: CommonDetailsModel): CommonDetails {
        val entity = CommonDetails()
        entity.apply {
            firebaseUserId = commonDetailsModel.firebaseUserId
            fcmToken = commonDetailsModel.fcmToken
        }
        return entity
    }
}