package com.example.newversity.entity.phonepe

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "phone_pe_transaction_details")
class PhonePeTransactionDetails (
        @Field(name = "merchant_transaction_id")
        var merchantTransactionId: String? = null,

        @Field(name = "phone_pe_transaction_details_data")
        var phonePeTransactionDetailsData: PhonePeTransactionDetailsDataEntity? = null
)

class PhonePeTransactionDetailsDataEntity (
        var success : Boolean? = null,
        var code    : String?  = null,
        var message : String?  = null,
        var data    : TransactionDataEntity?  = null,
)

class TransactionDataEntity (
        @Field(name = "merchant_id")
        var merchantId              : String? = null,

        @Field(name = "merchant_transaction_id")
        var merchantTransactionId   : String? = null,

        @Field(name = "transaction_id")
        var transactionId           : String? = null,

        var amount                  : Int?    = null,

        var state                   : String? = null,

        @Field(name = "response_code")
        var responseCode            : String? = null,

        @Field(name = "response_code_description")
        var responseCodeDescription : String? = null,

        @Field(name = "payment_instrument")
        var paymentInstrument       : HashMap<String, String?>? = null
)