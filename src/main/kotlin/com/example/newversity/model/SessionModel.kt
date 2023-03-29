package com.example.newversity.model

import com.example.newversity.entity.Session
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.*

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
class SessionModel (
        var id: String? = null,
        var teacherId: String? = null,
        var studentId: String? = null,
        var startDate: Date? = null,
        var endDate: Date? = null,
        var amount: Double? = null,
        var sessionType: String? = null,
        var agenda: String? = null,
        var paymentId: String? = null,
        var mentorNote: String? = null,
        var studentFeedback: String? = null,
        var studentRating: Int? = null,
        var cancelled: Boolean? = null,
)
object SessionConvertor {
    fun toEntity(sessionModel: SessionModel): Session {
        val entity = Session()
        entity.apply {
            id = sessionModel.id
            teacherId = sessionModel.teacherId
            studentId = sessionModel.studentId
            startDate = sessionModel.startDate
            endDate = sessionModel.endDate
            amount = sessionModel.amount
            sessionType = sessionModel.sessionType
            agenda = sessionModel.agenda
            paymentId = sessionModel.paymentId
            mentorNote = sessionModel.mentorNote
            studentFeedback = sessionModel.studentFeedback
            studentRating = sessionModel.studentRating
            cancelled = sessionModel.cancelled
        }
        return entity
    }

    fun toModel(session: Session): SessionModel {
        val model = SessionModel()
        model.apply {
            id = session.id
            teacherId = session.teacherId
            studentId = session.studentId
            startDate = session.startDate
            endDate = session.endDate
            amount = session.amount
            sessionType = session.sessionType
            agenda = session.agenda
            paymentId = session.paymentId
            mentorNote = session.mentorNote
            studentFeedback = session.studentFeedback
            studentRating = session.studentRating
            cancelled = session.cancelled
        }
        return model
    }
}