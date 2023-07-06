package com.example.newversity.services.teacher

import com.example.newversity.model.BankAccountDetailConvertor
import com.example.newversity.model.BankAccountDetailModel
import com.example.newversity.model.EmptyJsonResponse
import com.example.newversity.repository.BankAccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class BankAccountService(
        @Autowired val bankAccountRepository: BankAccountRepository
) {
    fun addBankAccountDetails(bankAccountDetailModel: BankAccountDetailModel, teacherId: String): ResponseEntity<*> {
        val bankAccountEntity = bankAccountRepository.findByTeacherId(teacherId)
        return if (bankAccountEntity.isPresent) {
            val bankAccount = bankAccountEntity.get()
            bankAccount.accountName = bankAccountDetailModel.accountName
            bankAccount.accountNumber = bankAccountDetailModel.accountNumber
            bankAccount.ifscCode = bankAccountDetailModel.ifscCode
            bankAccountRepository.save(bankAccount)
            ResponseEntity.ok(EmptyJsonResponse())
        } else {
            if(isBankAccountModelValid(bankAccountDetailModel)) {
                bankAccountRepository.save(BankAccountDetailConvertor.onEntity(bankAccountDetailModel))
                ResponseEntity.ok(EmptyJsonResponse())
            } else {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("status" to "Details Missing"))
            }
        }
    }

    fun isBankAccountModelValid(bankAccountDetailModel: BankAccountDetailModel): Boolean {
        val isDetailsMissing = bankAccountDetailModel.accountNumber.isNullOrEmpty() || bankAccountDetailModel.accountName.isNullOrEmpty()
                || bankAccountDetailModel.ifscCode.isNullOrEmpty() || bankAccountDetailModel.teacherId.isNullOrEmpty()
        return !isDetailsMissing
    }

    fun getBankAccountDetails(teacherId: String): ResponseEntity<*> {
        val bankAccountEntity = bankAccountRepository.findByTeacherId(teacherId)
        return if(bankAccountEntity.isPresent) {
            ResponseEntity.ok(BankAccountDetailConvertor.toModel(bankAccountEntity.get()))
        } else {
            ResponseEntity.ok(EmptyJsonResponse())
        }
    }
}