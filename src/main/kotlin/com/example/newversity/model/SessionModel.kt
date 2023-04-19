package com.example.newversity.model

import com.example.newversity.entity.Session
import com.example.newversity.entity.TeacherDetails
import com.example.newversity.entity.students.Student
import com.example.newversity.model.room.GenerateRoomResponseModel
import com.example.newversity.model.student.StudentConverter
import com.example.newversity.model.student.StudentDetailModel
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.*

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class SessionModel (
        var id: String? = null,
        var teacherId: String? = null,
        var studentId: String? = null,
        var teacherDetail: TeacherDetailModel? = null,
        var studentDetail: StudentDetailModel? = null,
        var teacherTagList: List<TagModel>? = null,
        var startDate: Date? = null,
        var endDate: Date? = null,
        var amount: Double? = null,
        var sessionType: String? = null,
        var agenda: String? = null,
        var paymentId: String? = null,
        var mentorNote: String? = null,
        var studentFeedback: String? = null,
        var studentRating: Double? = null,
        var issueRaised: String? = null,
        var cancelled: Boolean? = null,
        var teacherToken: String? = null,
        var studentToken: String? = null,
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
            issueRaised = sessionModel.issueRaised
            cancelled = sessionModel.cancelled
            teacherToken = sessionModel.teacherToken
            studentToken = sessionModel.studentToken
        }
        return entity
    }

    fun toModel(session: Session, teacherDetails: TeacherDetails?, student: Student?, tagList: List<TagModel>?): SessionModel {
        val model = SessionModel()
        model.apply {
            id = session.id
            teacherId = session.teacherId
            studentId = session.studentId
            teacherDetail = teacherDetails?.let { TeacherConverter.toModel(it) }
            studentDetail = student?.let { StudentConverter.toModel(it) }
            teacherTagList = tagList
            startDate = session.startDate
            endDate = session.endDate
            amount = session.amount
            sessionType = session.sessionType
            agenda = session.agenda
            paymentId = session.paymentId
            mentorNote = session.mentorNote
            studentFeedback = session.studentFeedback
            studentRating = session.studentRating
            issueRaised = session.issueRaised
            cancelled = session.cancelled
            teacherToken = session.teacherToken
            studentToken = session.studentToken
        }
        return model
    }
}