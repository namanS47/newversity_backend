package com.example.newversity.model.payment

import com.google.gson.annotations.SerializedName

data class PaymentOrderModel(
        @SerializedName("amount"      ) var amount     : Int?              = null,
        @SerializedName("amount_paid" ) var amountPaid : Int?              = null,
        @SerializedName("notes"       ) var notes      : Map<String, String>?            = null,
        @SerializedName("created_at"  ) var createdAt  : Int?              = null,
        @SerializedName("amount_due"  ) var amountDue  : Int?              = null,
        @SerializedName("currency"    ) var currency   : String?           = null,
        @SerializedName("receipt"     ) var receipt    : String?           = null,
        @SerializedName("id"          ) var id         : String?           = null,
        @SerializedName("entity"      ) var entity     : String?           = null,
        @SerializedName("offer_id"    ) var offerId    : String?           = null,
        @SerializedName("status"      ) var status     : String?           = null,
        @SerializedName("attempts"    ) var attempts   : Int?              = null
)
