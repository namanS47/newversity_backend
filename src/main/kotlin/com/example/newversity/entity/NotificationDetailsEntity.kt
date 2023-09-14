package com.example.newversity.entity

import com.example.newversity.entity.teacher.AppEntity
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.util.*
import kotlin.collections.HashMap

@Document(collection = "notifications")
class NotificationDetailsEntity (
        @Field("user_id")
        var userId: String? = null,

        var title: String? = null,

        var body: String? = null,

        var data: HashMap<String, String> = hashMapOf(),
): AppEntity()