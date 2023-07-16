package com.example.newversity.model.payment.phonepe

import com.example.newversity.entity.phonepe.PhonePeTransactionDetails
import com.example.newversity.entity.phonepe.PhonePeTransactionDetailsDataEntity
import com.example.newversity.entity.phonepe.TransactionDataEntity
import com.google.gson.annotations.SerializedName

data class PhonePeTransactionStatusModel (
        @SerializedName("merchant_transaction_id") var merchantTransactionId: String? = null,

        @SerializedName("phone_pe_transaction_status_response")
        var phonePeTransactionStatusResponse: PhonePeTransactionStatusResponseModel? = null
)

data class PhonePeTransactionStatusResponseModel(
        @SerializedName("success" ) var success : Boolean? = null,
        @SerializedName("code"    ) var code    : String?  = null,
        @SerializedName("message" ) var message : String?  = null,
        @SerializedName("data"    ) var data    : TransactionData?    = null,
)

data class TransactionData (

        @SerializedName("merchantId"              ) var merchantId              : String? = null,
        @SerializedName("merchantTransactionId"   ) var merchantTransactionId   : String? = null,
        @SerializedName("transactionId"           ) var transactionId           : String? = null,
        @SerializedName("amount"                  ) var amount                  : Int?    = null,
        @SerializedName("state"                   ) var state                   : String? = null,
        @SerializedName("responseCode"            ) var responseCode            : String? = null,
        @SerializedName("responseCodeDescription" ) var responseCodeDescription : String? = null,
        @SerializedName("paymentInstrument"       ) var paymentInstrument       : HashMap<String, String?>? = null

)

object PhonePeTransactionStatusModelConvertor {
        fun toEntity(phonePeTransactionStatusModel: PhonePeTransactionStatusModel): PhonePeTransactionDetails {
                val entity = PhonePeTransactionDetails()
                entity.apply {
                        merchantTransactionId = phonePeTransactionStatusModel.merchantTransactionId
                        phonePeTransactionDetailsData = phonePeTransactionStatusModel.phonePeTransactionStatusResponse?.let { PhonePeTransactionStatusResponseModelConvertor.toEntity(it) }
                }
                return entity
        }
}

object PhonePeTransactionStatusResponseModelConvertor {
        fun toEntity(phonePeTransactionStatusResponseModel: PhonePeTransactionStatusResponseModel): PhonePeTransactionDetailsDataEntity {
                val entity = PhonePeTransactionDetailsDataEntity()
                entity.apply {
                        success = phonePeTransactionStatusResponseModel.success
                        code = phonePeTransactionStatusResponseModel.code
                        message = phonePeTransactionStatusResponseModel.message
                        data = phonePeTransactionStatusResponseModel.data?.let { TransactionDataConvertor.toEntity(it) }
                }
                return entity
        }
}

object TransactionDataConvertor{
        fun toEntity(transactionData: TransactionData) : TransactionDataEntity {
                val entity = TransactionDataEntity()
                entity.apply {
                        merchantId = transactionData.merchantId
                        merchantTransactionId = transactionData.merchantTransactionId
                        transactionId = transactionData.transactionId
                        amount = transactionData.amount
                        state = transactionData.state
                        responseCode = transactionData.responseCode
                        responseCodeDescription = transactionData.responseCodeDescription
                        paymentInstrument = transactionData.paymentInstrument
                }
                return entity
        }
}



