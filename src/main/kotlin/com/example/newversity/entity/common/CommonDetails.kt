package com.example.newversity.entity.common

import com.example.newversity.entity.teacher.AppEntity
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "common")
class CommonDetails (
        @Field("firebase_user_id")
        var firebaseUserId: String? = null,

        @Field("fcm_token")
        var fcmToken: String? = null
): AppEntity()