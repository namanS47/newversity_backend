package com.example.NewVersity.model

import com.example.NewVersity.entity.TeacherExperience
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.*

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
class TeacherExperienceModel(
        var id: String? = null,
        var teacherId: String? = null,
        var title: String? = null,
        var employmentType: String? = null,
        var companyName: String? = null,
        var location: String? = null,
        var startDate: Date? = null,
        var endDate: Date? = null,
        var currentlyWorkingHere: Boolean? = null,
)

object TeacherExperienceConverter {
    fun toEntity(teacherExperienceModel: TeacherExperienceModel): TeacherExperience {
        val entity = TeacherExperience()
        entity.apply {
            id = teacherExperienceModel.id
            teacherId = teacherExperienceModel.teacherId
            title = teacherExperienceModel.title
            employmentType = teacherExperienceModel.employmentType
            companyName = teacherExperienceModel.companyName
            location = teacherExperienceModel.location
            startDate = teacherExperienceModel.startDate
            endDate = teacherExperienceModel.endDate
            currentlyWorkingHere = teacherExperienceModel.currentlyWorkingHere
        }
        return entity
    }

    fun toModel(teacherExperience: TeacherExperience): TeacherExperienceModel {
        val model = TeacherExperienceModel()
        model.apply {
            id = teacherExperience.id
            teacherId = teacherExperience.teacherId
            title  =teacherExperience.title
            employmentType = teacherExperience.employmentType
            companyName = teacherExperience.companyName
            location = teacherExperience.location
            startDate = teacherExperience.startDate
            endDate = teacherExperience.endDate
            currentlyWorkingHere = teacherExperience.currentlyWorkingHere
        }
        return model
    }
}