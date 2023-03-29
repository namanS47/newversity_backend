package com.example.newversity.entity
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

        @Field("payment_id")
        var paymentId: String? = null,

        @Field("mentor_note")
        var mentorNote: String? = null,

        @Field("student_feedback")
        var studentFeedback: String? = null,

        @Field("student_rating")
        var studentRating: Int? = null,

        var cancelled: Boolean? = null,
) : AppEntity()