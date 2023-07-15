package com.example.newversity.model.payment.phonepe

import com.example.newversity.entity.phonepe.DataEntity
import com.example.newversity.entity.phonepe.InstrumentResponseEntity
import com.example.newversity.entity.phonepe.PhonePeOrder
import com.example.newversity.entity.phonepe.RedirectInfoEntity
import com.google.gson.annotations.SerializedName

data class PhonePePGUrlResponseModel (
        @SerializedName("success" ) var success : Boolean? = null,
        @SerializedName("code"    ) var code    : String?  = null,
        @SerializedName("message" ) var message : String?  = null,
        @SerializedName("data"    ) var data    : Data?    = Data()
)

data class Data (

        @SerializedName("merchantId"            ) var merchantId            : String?             = null,
        @SerializedName("merchantTransactionId" ) var merchantTransactionId : String?             = null,
        @SerializedName("instrumentResponse"    ) var instrumentResponse    : InstrumentResponse? = InstrumentResponse()

)

data class InstrumentResponse (

        @SerializedName("type"         ) var type         : String?       = null,
        @SerializedName("redirectInfo" ) var redirectInfo : RedirectInfo? = RedirectInfo()

)

data class RedirectInfo (

        @SerializedName("url"    ) var url    : String? = null,
        @SerializedName("method" ) var method : String? = null

)

object PhonePePGUrlResponseModelConvertor {
    fun toEntity(phonePePGUrlResponseModel: PhonePePGUrlResponseModel): PhonePeOrder {
        val entity = PhonePeOrder()
        entity.apply {
            success = phonePePGUrlResponseModel.success
            code = phonePePGUrlResponseModel.code
            message = phonePePGUrlResponseModel.message
            data = DataConvertor.toEntity(phonePePGUrlResponseModel.data)
        }
        return entity
    }
}

object DataConvertor {
    fun toEntity(data: Data?): DataEntity {
        val entity = DataEntity()
        entity.apply {
            merchantId = data?.merchantId
            merchantTransactionId = data?.merchantTransactionId
            instrumentResponse = InstrumentResponseConvertor.toEntity(data?.instrumentResponse)
        }
        return  entity
    }
}

object InstrumentResponseConvertor {
    fun toEntity(instrumentResponse: InstrumentResponse?) : InstrumentResponseEntity {
        val entity = InstrumentResponseEntity()
        entity.apply {
            type = instrumentResponse?.type
            redirectInfo = RedirectInfoConvertor.toEntity(instrumentResponse?.redirectInfo)
        }
        return entity
    }
}

object RedirectInfoConvertor {
    fun toEntity(redirectInfo: RedirectInfo?) : RedirectInfoEntity {
        val entity = RedirectInfoEntity()
        entity.apply {
            url = redirectInfo?.url
            method = redirectInfo?.method
        }
        return entity
    }
}
