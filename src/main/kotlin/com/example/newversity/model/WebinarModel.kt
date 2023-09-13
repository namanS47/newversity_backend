package com.example.newversity.model

import com.example.newversity.entity.common.StudentInfo
import com.example.newversity.entity.common.WebinarDetails
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.Date

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class WebinarModel (
        var id: String? = null,
        var teacherId: String? = null,
        var teacherName: String? = null,
        var teacherTitle: String? = null,
        var teacherProfilePicture: String? = null,
        var webinarDate: Date? = null,
        var studentsInfoList: ArrayList<StudentInfoModel>? = null,
        var title: String? = null,
        var feedback: String? = null,
        var rating: Double? = null,
        var agenda: String? = null,
        var issueRaised: String? = null,
        var shareLink: String? = null,
        var joiningLink: String? = null,
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class StudentInfoModel (
        var studentId: String? = null,
        var info: String? = null,
        var agenda: String? = null,
        var location: String? = null,
        var review: String? = null,
)

object WebinarConvertor {
    fun toEntity(webinarModel: WebinarModel) : WebinarDetails {
        val entity = WebinarDetails()
        entity.apply {
            teacherId= webinarModel.teacherId
            teacherName = webinarModel.teacherName
            teacherTitle = webinarModel.teacherTitle
            teacherProfilePicture = webinarModel.teacherProfilePicture
            webinarDate = webinarModel.webinarDate
            studentsInfoList = webinarModel.studentsInfoList?.map {
                StudentInfoModelConvertor.toEntity(it)
            }?.let { ArrayList(it) }
            title= webinarModel.title
            feedback = webinarModel.feedback
            rating = webinarModel.rating
            agenda = webinarModel.agenda
            issueRaised = webinarModel.issueRaised
            shareLink = webinarModel.shareLink
            joiningLink = webinarModel.joiningLink
        }
        return entity
    }

    fun toModel(webinarDetails: WebinarDetails) : WebinarModel {
        val model = WebinarModel()
        model.apply {
            id = webinarDetails.id
            teacherId= webinarDetails.teacherId
            teacherName = webinarDetails.teacherName
            teacherTitle = webinarDetails.teacherTitle
            teacherProfilePicture = webinarDetails.teacherProfilePicture
            webinarDate = webinarDetails.webinarDate
            studentsInfoList = webinarDetails.studentsInfoList?.map {
                StudentInfoModelConvertor.toModel(it)
            }?.let { ArrayList(it) }
            title= webinarDetails.title
            feedback = webinarDetails.feedback
            rating = webinarDetails.rating
            issueRaised = webinarDetails.issueRaised
            agenda = webinarDetails.agenda
            shareLink = webinarDetails.shareLink
            joiningLink = webinarDetails.joiningLink
        }
        return model
    }
}

object StudentInfoModelConvertor{
    fun toEntity(studentInfoModel: StudentInfoModel): StudentInfo {
        val studentInfo = StudentInfo()
        studentInfo.apply {
            studentId = studentInfoModel.studentId
            info = studentInfoModel.info
            agenda = studentInfoModel.agenda
            location = studentInfoModel.location
            review = studentInfoModel.review
        }
        return studentInfo
    }

    fun toModel(studentInfo: StudentInfo): StudentInfoModel {
        val studentInfoModel = StudentInfoModel()
        studentInfoModel.apply {
            studentId = studentInfo.studentId
            info = studentInfo.info
            agenda = studentInfo.agenda
            location = studentInfo.location
            review = studentInfoModel.review
        }
        return studentInfoModel
    }
}