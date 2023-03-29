package com.example.NewVersity.entity
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.util.*

@Document(collection = "session")
class Session(
        @Field("teacher_id")
        var teacherId                   :String?                        = null,

        @Field("student_id")
        var studentId: String? = null,

        @Field("start_date")
        var startDate: Date? = null,

        @Field("end_date")
        var endDate: Date? = null,

        var amount: Double? = null,

        @Field("session_type")
        var sessionType: String? = null,

        var agenda: String? = null,
)