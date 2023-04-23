package com.example.newversity.entity
import com.example.newversity.model.room.GenerateRoomResponseModel
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

        @Field("order_id")
        var orderId: String? = null,

        @Field("mentor_note")
        var mentorNote: String? = null,

        @Field("student_feedback")
        var studentFeedback: String? = null,

        @Field("student_rating")
        var studentRating: Double? = null,

        @Field("issue_raised")
        var issueRaised: String? = null,

        var cancelled: Boolean? = null,

        @Field("teacher_token")
        var teacherToken: String? = null,

        @Field("student_token")
        var studentToken: String? = null,

        var roomResponseModel: GenerateRoomResponseModel? = null,
) : AppEntity()