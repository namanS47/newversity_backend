package com.example.NewVersity.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.util.*
import kotlin.collections.HashMap

@Document(collection = "teacher")
class TeacherDetails(
        @Field("teacher_id")
        var teacherId                   :String?                        = null,

        var name                        : String?                       = null,

        @Field("mobile_number")
        var mobileNumber                : String?                       = null,

        var email                       :String?                        = null,

        @Field("intro_video_url")
        var introVideoUrl               : String?                       = null,

        var location                    : String?                       = null,

        var title                       : String?                       = null,

        var info                        : String?                       = null,

        var tags                        :List<String>?                  = null,

        var education                   :String?                        = null,

        var designation                   :String?                        = null,

        @Field("profile_url")
        var profileUrl                  : String?                       = null,

        @Field("profile_picture_url")
        var profilePictureUrl           : String?                       = null,

        @Field("session_pricing")
        var sessionPricing              : HashMap<String, Double>?      = null,

        var language                    : List<String>?                 = null
): AppEntity()
