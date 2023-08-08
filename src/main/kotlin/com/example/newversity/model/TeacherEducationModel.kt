package com.example.newversity.model

import com.example.newversity.entity.teacher.TeacherEducation
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.*

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class TeacherEducationModel(
        var id: String? = null,
        var teacherId: String? = null,
        var name: String? = null,
        var degree: String? = null,
        var startDate: Date? = null,
        var endDate: Date? = null,
        var currentlyWorkingHere: Boolean? = null,
        var grade: String? = null,
)

object TeacherEducationConvertor {
    fun toEntity(teacherEducationModel: TeacherEducationModel): TeacherEducation {
        val entity = TeacherEducation()
        entity.apply {
            id = teacherEducationModel.id
            teacherId = teacherEducationModel.teacherId
            name = teacherEducationModel.name
            degree = teacherEducationModel.degree
            startDate = teacherEducationModel.startDate
            endDate = teacherEducationModel.endDate
            currentlyWorkingHere = teacherEducationModel.currentlyWorkingHere
            grade = teacherEducationModel.grade
        }
        return entity
    }

    fun toModel(teacherEducation: TeacherEducation): TeacherEducationModel {
        val model = TeacherEducationModel()
        model.apply {
            id = teacherEducation.id
            teacherId = teacherEducation.teacherId
            name = teacherEducation.name
            degree = teacherEducation.degree
            startDate = teacherEducation.startDate
            endDate = teacherEducation.endDate
            currentlyWorkingHere = teacherEducation.currentlyWorkingHere
            grade = teacherEducation.grade
        }
        return model
    }
}