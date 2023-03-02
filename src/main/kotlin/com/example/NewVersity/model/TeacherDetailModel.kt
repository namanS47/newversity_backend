package com.example.NewVersity.model

import com.example.NewVersity.entity.TeacherDetails
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.*


@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class TeacherDetailModel(
        var teacherId: String? = null,
        var name: String? = null,
        var mobileNumber: String? = null,
        var email: String? = null,
        var introVideoUrl: String? = null,
        var location: String? = null,
        var title: String? = null,
        var info: String? = null,
        var tags: List<String>? = null,
        var education: String? = null,
        var designation: String? = null,
        var profileUrl: String? = null,
        var profilePictureUrl: String? = null,
        var sessionPricing: HashMap<String, Double>? = null,
        var language: List<String>? = null
)

object TeacherConverter {
    fun toEntity(teacherDetailModel: TeacherDetailModel):TeacherDetails {
        val entity= TeacherDetails()

        entity.apply {
            teacherId = teacherDetailModel.teacherId
            name = teacherDetailModel.name
            mobileNumber = teacherDetailModel.mobileNumber
            email  =teacherDetailModel.email
            introVideoUrl = teacherDetailModel.introVideoUrl
            location = teacherDetailModel.location
            title = teacherDetailModel.title
            info = teacherDetailModel.info
            tags = teacherDetailModel.tags
            education = teacherDetailModel.education
            designation = teacherDetailModel.designation
            profileUrl = teacherDetailModel.profileUrl
            profilePictureUrl = teacherDetailModel.profilePictureUrl
            sessionPricing = teacherDetailModel.sessionPricing
            language = teacherDetailModel.language
        }
        return entity
    }

    fun toModel(teacherDetails: TeacherDetails) : TeacherDetailModel {
        val model= TeacherDetailModel()
        model.apply {
            teacherId = teacherDetails.teacherId
            name = teacherDetails.name
            mobileNumber = teacherDetails.mobileNumber
            email = teacherDetails.email
            introVideoUrl = teacherDetails.introVideoUrl
            location = teacherDetails.location
            title = teacherDetails.title
            info = teacherDetails.info
            tags = teacherDetails.tags
            education = teacherDetails.education
            designation = teacherDetails.designation
            profileUrl = teacherDetails.profileUrl
            profilePictureUrl = teacherDetails.profilePictureUrl
            sessionPricing = teacherDetails.sessionPricing
            language = teacherDetails.language
        }
        return model
    }
}