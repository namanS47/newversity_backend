package com.example.newversity.services.phonepe

import com.example.newversity.model.payment.phonepe.*
import com.example.newversity.repository.phonepe.PhonePeOrderRepository
import com.example.newversity.repository.phonepe.PhonePeTransactionRepository
import com.google.gson.Gson
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import java.security.MessageDigest
import java.util.*

@Service
class PhonePeService(
        @Autowired val phonePeOrderRepository: PhonePeOrderRepository,
        @Autowired val phonePeTransactionRepository: PhonePeTransactionRepository
) {
    @Value("\${phone_pe_merchant_id}")
    private lateinit var merchantId: String

    @Value("\${phone_pe_salt_key}")
    private lateinit var saltKey: String

    @Value("\${server_url}")
    private lateinit var newversityBaseUrl: String

    @Value("\${phone_pe_base_url}")
    private lateinit var phonePeBaseUrl: String

    var logger: Logger = LoggerFactory.getLogger(PhonePeService::class.java)

    fun fetchPhonePePaymentUrl(phonePePGUrlRequestModel: PhonePePGUrlRequestModel) : ResponseEntity<*> {
        phonePePGUrlRequestModel.merchantId = merchantId
        phonePePGUrlRequestModel.redirectUrl = "https://www.newversity.in"
        phonePePGUrlRequestModel.callbackUrl = "${newversityBaseUrl}/phonePeCallbackUrl"
        phonePePGUrlRequestModel.redirectMode = "POST"
        phonePePGUrlRequestModel.paymentInstrument["type"] = "PAY_PAGE"

        val base64EncodedPayload = getBase64EncodedPayload(phonePePGUrlRequestModel)

        val encryptedPayload = getEncryptedPayloadHash(base64EncodedPayload)

        val restTemplate = RestTemplate()
        val headers = LinkedMultiValueMap<String, String>()

        headers["Content-Type"] = MediaType.APPLICATION_JSON.toString()
        headers["X-VERIFY"] = encryptedPayload
        headers["accept"] = MediaType.APPLICATION_JSON.toString()

        val map: HashMap<String, Any> = HashMap()
        map["request"] = base64EncodedPayload

        val uri = "$phonePeBaseUrl/pg/v1/pay"

        val entity: HttpEntity<Map<String, Any>> = HttpEntity(map, headers)
        val response = restTemplate.postForEntity(uri, entity, String::class.java)
        val phonePePGUrlResponseModel = Gson().fromJson(response.body, PhonePePGUrlResponseModel::class.java)

        logger.debug(phonePePGUrlResponseModel.toString())

        return if(phonePePGUrlResponseModel.success == true) {
            phonePeOrderRepository.save(PhonePePGUrlResponseModelConvertor.toEntity(phonePePGUrlResponseModel))
            ResponseEntity.ok().body(PhonePeCallbackResponseModel(phonePePGUrlResponseModel.data?.instrumentResponse?.redirectInfo?.url))
        } else {
            if(phonePePGUrlResponseModel.code == "BAD_REQUEST" || phonePePGUrlResponseModel.code == "AUTHORIZATION_FAILED") {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("")
            } else {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong")
            }
        }
    }


    fun getBase64EncodedPayload(phonePePGUrlRequestModel: PhonePePGUrlRequestModel): String {
        val encoder: Base64.Encoder = Base64.getEncoder()
        return encoder.encodeToString(Gson().toJson(phonePePGUrlRequestModel).toByteArray())
    }

    fun getEncryptedPayloadHash(base64Payload: String): String {
        var encryptedPayload = hashString("$base64Payload/pg/v1/pay$saltKey", "SHA-256")
        encryptedPayload+= "###" + 1
        return encryptedPayload
    }

    private fun hashString(input: String, algorithm: String): String {
        return MessageDigest
                .getInstance(algorithm)
                .digest(input.toByteArray())
                .fold("") { str, it -> str + "%02x".format(it) }
    }

    fun handleCallbackResponseModel(callbackResponseModel: PhonePeCallbackResponseModel) {
        val decoder = Base64.getDecoder()
        val decoded =String(decoder.decode(callbackResponseModel.response))
        val phonePeTransactionStatusResponseModel = Gson().fromJson(decoded, PhonePeTransactionStatusResponseModel::class.java)
        val phonePeTransactionStatusModel = PhonePeTransactionStatusModel(
                merchantTransactionId = phonePeTransactionStatusResponseModel.data?.merchantTransactionId,
                phonePeTransactionStatusResponse = phonePeTransactionStatusResponseModel
        )
        phonePeTransactionRepository.save(PhonePeTransactionStatusModelConvertor.toEntity(phonePeTransactionStatusModel))
    }

    fun checkTransactionStatusApi(merchantTransactionId: String): ResponseEntity<*> {
        val transactionDetails = phonePeTransactionRepository.findAllByMerchantTransactionId(merchantTransactionId)
        if(transactionDetails.isPresent){
            val transactionDetailsEntity = transactionDetails.get()

            return if(transactionDetailsEntity.phonePeTransactionDetailsData?.code != "PAYMENT_PENDING") {
                ResponseEntity.ok().body(transactionDetailsEntity)
            } else {
                val transactionStatus = fetchTransactionStatus(merchantTransactionId)
                transactionDetailsEntity.phonePeTransactionDetailsData =
                        PhonePeTransactionStatusResponseModelConvertor.toEntity(transactionStatus)

                phonePeTransactionRepository.save(transactionDetailsEntity)

                ResponseEntity.ok().body(transactionDetailsEntity)
            }
        } else {
            val transactionStatus = fetchTransactionStatus(merchantTransactionId)
            val phonePeTransactionStatusModel = PhonePeTransactionStatusModel(
                    merchantTransactionId = transactionStatus.data?.merchantTransactionId,
                    phonePeTransactionStatusResponse = transactionStatus
            )
            val phonePeTransactionStatusEntity = PhonePeTransactionStatusModelConvertor.toEntity(phonePeTransactionStatusModel)
            phonePeTransactionRepository.save(phonePeTransactionStatusEntity)

            return ResponseEntity.ok().body(phonePeTransactionStatusEntity)
        }
    }

    fun fetchTransactionStatus(merchantTransactionId: String): PhonePeTransactionStatusResponseModel {
        val headerString = "/pg/v1/status/$merchantId/$merchantTransactionId$saltKey"
        val encrypted = hashString(headerString, "SHA-256") + "###1"
        val restTemplate = RestTemplate()
        val headers = LinkedMultiValueMap<String, String>()

        headers["Content-Type"] = MediaType.APPLICATION_JSON.toString()
        headers["X-VERIFY"] = encrypted
        headers["accept"] = MediaType.APPLICATION_JSON.toString()
        headers["X-MERCHANT-ID"] = merchantId

        val uri = "$phonePeBaseUrl/pg/v1/status/$merchantId/$merchantTransactionId"

        val entity: HttpEntity<Map<String, Any>> = HttpEntity(headers)
        val response = restTemplate.exchange(uri, HttpMethod.GET, entity, String::class.java)
        return Gson().fromJson(response.body, PhonePeTransactionStatusResponseModel::class.java)
    }
}