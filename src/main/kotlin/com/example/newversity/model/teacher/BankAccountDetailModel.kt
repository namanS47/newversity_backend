package com.example.newversity.model.teacher

import com.example.newversity.entity.teacher.BankAccountDetail
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class BankAccountDetailModel(
        var id: String? = null,
        var teacherId: String? = null,
        var accountNumber: String? = null,
        var accountName: String? = null,
        var ifscCode: String? = null,
)

object BankAccountDetailConvertor {
    fun onEntity(bankAccountDetailModel: BankAccountDetailModel) : BankAccountDetail {
        val bankAccountDetail = BankAccountDetail()
        bankAccountDetail.apply {
            id = bankAccountDetailModel.id
            teacherId = bankAccountDetailModel.teacherId
            accountNumber = bankAccountDetailModel.accountNumber
            accountName = bankAccountDetailModel.accountName
            ifscCode = bankAccountDetailModel.ifscCode
        }
        return  bankAccountDetail
    }

    fun toModel(bankAccountDetail: BankAccountDetail): BankAccountDetailModel {
        val bankAccountDetailModel = BankAccountDetailModel()
        bankAccountDetailModel.apply {
            id = bankAccountDetail.id
            teacherId = bankAccountDetail.teacherId
            accountNumber = bankAccountDetail.accountNumber
            accountName = bankAccountDetail.accountName
            ifscCode = bankAccountDetail.ifscCode
        }
        return bankAccountDetailModel
    }
}