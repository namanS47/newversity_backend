package com.example.newversity.entity.students

import com.example.newversity.entity.teacher.AppEntity
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "student")
class Student(
        @Field("student_id")
        var studentId                   :String?                        = null,

        var name                        : String?                       = null,

        @Field("mobile_number")
        var mobileNumber                : String?                       = null,

        var email                       :String?                        = null,

        var tags                        :List<String>?                  = null,

        var location                    : String?                       = null,

        var language                   : List<String>?                  = null,

        var info                       : String?                        = null,

        @Field("profile_picture_url")
        var profilePictureUrl           : String?                       = null,
) : AppEntity()