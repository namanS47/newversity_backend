package com.example.newversity.repository.phonepe

import com.example.newversity.entity.phonepe.PhonePeTransactionDetails
import org.springframework.data.mongodb.repository.MongoRepository

interface PhonePeTransactionRepository: MongoRepository<PhonePeTransactionDetails, String> {
}