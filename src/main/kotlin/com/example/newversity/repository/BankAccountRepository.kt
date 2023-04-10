package com.example.newversity.repository

import com.example.newversity.entity.BankAccountDetail
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface BankAccountRepository: MongoRepository<BankAccountDetail, String> {
    fun findByTeacherId(teacherId: String) : Optional<BankAccountDetail>
}