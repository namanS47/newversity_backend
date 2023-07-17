package com.example.newversity.repository.phonepe

import com.example.newversity.entity.phonepe.PhonePeTransactionDetails
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface PhonePeTransactionRepository: MongoRepository<PhonePeTransactionDetails, String> {
    fun findAllByMerchantTransactionId(merchantTransactionId: String): Optional<PhonePeTransactionDetails>
}