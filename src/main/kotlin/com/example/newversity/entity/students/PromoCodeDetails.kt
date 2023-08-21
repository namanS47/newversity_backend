package com.example.newversity.entity.students

import com.example.newversity.entity.teacher.AppEntity
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "promo_code")
class PromoCodeDetails (
        @Field("promo_code")
        var promoCode: String? = null,

        @Field("entity_name")
        var entityName: String? = null,

        @Field("entity_code")
        var entityCode: String? = null,

        @Field("")
        var percentageDiscount: Int? = null,

        @Field("is_expired")
        var isExpired: Boolean? = null,
): AppEntity()