package com.example.newversity.model.student

import com.example.newversity.entity.students.Student
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
class StudentDetailModel(
        var studentId: String? = null,
        var name: String? = null,
        var mobileNumber: String? = null,
        var email: String? = null,
        var location: String? = null,
        var tags: List<String>? = null,
        var profilePictureUrl: String? = null,
        var language: List<String>? = null,
        var info: String? = null,
)

object StudentConverter {
    fun toEntity(studentDetailModel: StudentDetailModel): Student {
        val entity = Student()
        entity.apply {
            studentId = studentDetailModel.studentId
            name = studentDetailModel.name
            mobileNumber = studentDetailModel.mobileNumber
            email = studentDetailModel.email
            location = studentDetailModel.location
            tags = studentDetailModel.tags
            profilePictureUrl = studentDetailModel.profilePictureUrl
            language = studentDetailModel.language
            info = studentDetailModel.info
        }
        return entity
    }

    fun toModel(student: Student): StudentDetailModel {
        val model = StudentDetailModel()
        model.apply {
            studentId = student.studentId
            name = student.name
            mobileNumber = student.mobileNumber
            email = student.email
            location = student.location
            tags = student.tags
            profilePictureUrl = student.profilePictureUrl
            language = student.language
            info = student.info
        }
        return model
    }
}