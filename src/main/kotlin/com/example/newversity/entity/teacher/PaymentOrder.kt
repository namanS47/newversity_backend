package com.example.newversity.entity.teacher

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "payment_order")
class PaymentOrder(
        var amount     : Int?              = null,

        @Field("amount_paid" )
        var amountPaid : Int?              = null,

        var notes      : Map<String, String>? = null,

        @Field("created_at"  )
        var createdAt  : Int?              = null,

        @Field("amount_due"  )
        var amountDue  : Int?              = null,

        var currency   : String?           = null,

        var receipt    : String?           = null,

        var id         : String?           = null,

        var entity     : String?           = null,

        @Field("offer_id"    )
        var offerId    : String?           = null,

        var status     : String?           = null,

        var attempts   : Int?              = null
)