package com.example.newversity.entity

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "bank_account")
class BankAccountDetail(
        @Field("teacher_id")
        var teacherId: String? = null,

        @Field("account_number")
        var accountNumber: String? = null,

        @Field("account_name")
        var accountName: String? = null,

        @Field("ifsc_code")
        var ifscCode: String? = null,
) : AppEntity()