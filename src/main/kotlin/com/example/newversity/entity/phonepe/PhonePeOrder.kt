package com.example.newversity.entity.phonepe

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "phone_pe_order")
class PhonePeOrder(
        var success : Boolean? = null,

        var code    : String?  = null,

        var message : String?  = null,

        var data    : DataEntity?    = null,
)

class DataEntity (
        var merchantId            : String?             = null,

        var merchantTransactionId : String?             = null,

        @Field(name = "instrument_response")
        var instrumentResponse    : InstrumentResponseEntity? = null,
)

class InstrumentResponseEntity (
        var type         : String?       = null,

        @Field(name = "redirect_info")
        var redirectInfo : RedirectInfoEntity? = null
)

class RedirectInfoEntity (
        var url    : String? = null,

        var method : String? = null
)