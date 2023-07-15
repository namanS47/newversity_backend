package com.example.newversity.model.payment.phonepe

data class PhonePePGUrlRequestModel(
        var merchantId: String? = null,
        var merchantTransactionId: String? = null,
        var merchantUserId: String? = null,
        var amount: Int? = null,
        var redirectUrl: String? = null,
        var redirectMode: String? = null,
        var callbackUrl: String? = null,
        var mobileNumber: String? = null,
        var paymentInstrument: HashMap<String, String> = hashMapOf()
)
