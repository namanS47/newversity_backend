package com.example.newversity.model.student

import com.example.newversity.entity.students.PromoCodeDetails
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PromoCodeModel(
        var promoCode: String? = null,
        var entityName: String? = null,
        var entityCode: String? = null,
        var percentageDiscount: Int? = null,
        var isExpired: Boolean? = null,

)

object PromoCodeConvertor {
    fun toEntity(promoCodeModel: PromoCodeModel): PromoCodeDetails {
        val entity = PromoCodeDetails()
        entity.apply {
            promoCodeModel.promoCode?.let {
                promoCode = it
            }
            promoCodeModel.entityName?.let {
                entityName = it
            }
            promoCodeModel.entityCode?.let {
                entityCode = it
            }
            promoCodeModel.percentageDiscount?.let {
                percentageDiscount = it
            }
            promoCodeModel.isExpired?.let {
                isExpired = it
            }
        }
        return entity
    }

    fun toModel(promoCodeDetails: PromoCodeDetails): PromoCodeModel {
        val model = PromoCodeModel()
        model.apply {
            promoCode = promoCodeDetails.promoCode
            entityName = promoCodeDetails.entityName
            entityCode = promoCodeDetails.entityCode
            percentageDiscount = promoCodeDetails.percentageDiscount
            isExpired = promoCodeDetails.isExpired
        }
        return model
    }
}