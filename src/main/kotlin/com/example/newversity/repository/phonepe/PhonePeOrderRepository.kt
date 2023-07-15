package com.example.newversity.repository.phonepe

import com.example.newversity.entity.phonepe.PhonePeOrder
import org.springframework.data.mongodb.repository.MongoRepository

interface PhonePeOrderRepository: MongoRepository<PhonePeOrder, String> {}