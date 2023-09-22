package com.example.newversity.model.teacher

import com.example.newversity.entity.teacher.SharedContentEntity
import com.example.newversity.entity.teacher.TeacherDetails
import com.example.newversity.model.TagModel
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.*
import kotlin.collections.ArrayList


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TeacherDetailModel(
        var userId: String? = null,
        var teacherId: String? = null,
        var name: String? = null,
        var mobileNumber: String? = null,
        var email: String? = null,
        var introVideoUrl: String? = null,
        var location: String? = null,
        var gender: String? = null,
        var age: String? = null,
        var title: String? = null,
        var info: String? = null,
        var uploadedDocuments: List<String>? = null,
        var tags: List<TagModel>? = null,
        var education: String? = null,
        var designation: String? = null,
        var profileUrl: String? = null,
        var profilePictureUrl: String? = null,
        var sessionPricing: HashMap<String, Double>? = null,
        var language: List<String>? = null,
        var isNew: Boolean? = null,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "IST")
        var nextAvailable: Date? = null,
        var isApproved: Boolean? = null,
        var level: Int? = null,
        var userName: String? = null,
        var engagementType: List<EngagementType>? = null,
        var sharedContent: ArrayList<SharedContent>? = null,
)

object TeacherConverter {
    fun toEntity(teacherDetailModel: TeacherDetailModel): TeacherDetails {
        val entity= TeacherDetails()

        entity.apply {
            userId = teacherDetailModel.userId
            teacherId = teacherDetailModel.teacherId
            name = teacherDetailModel.name
            mobileNumber = teacherDetailModel.mobileNumber
            email  =teacherDetailModel.email
            introVideoUrl = teacherDetailModel.introVideoUrl
            location = teacherDetailModel.location
            gender = teacherDetailModel.gender
            age = teacherDetailModel.age
            title = teacherDetailModel.title
            info = teacherDetailModel.info
            uploadedDocuments = teacherDetailModel.uploadedDocuments
            tags = teacherDetailModel.tags
            education = teacherDetailModel.education
            designation = teacherDetailModel.designation
            profileUrl = teacherDetailModel.profileUrl
            profilePictureUrl = teacherDetailModel.profilePictureUrl
            sessionPricing = teacherDetailModel.sessionPricing
            language = teacherDetailModel.language
            isNew = teacherDetailModel.isNew
            isApproved = teacherDetailModel.isApproved
            level = teacherDetailModel.level
            userName = teacherDetailModel.userName
            engagementType = teacherDetailModel.engagementType
            sharedContent = teacherDetailModel.sharedContent?.map { SharedContentConvertor.toEntity(it) }?.let { ArrayList(it) }
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
            gender = teacherDetails.gender
            age = teacherDetails.age
            title = teacherDetails.title
            info = teacherDetails.info
            uploadedDocuments = teacherDetails.uploadedDocuments
            tags = teacherDetails.tags
            education = teacherDetails.education
            designation = teacherDetails.designation
            profileUrl = teacherDetails.profileUrl
            profilePictureUrl = teacherDetails.profilePictureUrl
            sessionPricing = teacherDetails.sessionPricing
            language = teacherDetails.language
            isNew = teacherDetails.isNew
            isApproved = teacherDetails.isApproved
            level = teacherDetails.level
            userName = teacherDetails.userName
            engagementType = teacherDetails.engagementType
            sharedContent = teacherDetails.sharedContent?.map { SharedContentConvertor.toModel(it) }?.let { ArrayList(it) }
        }
        return model
    }
}

data class SharedContent(
        var fileUrl: List<String>? = null,
        var title: String? = null,
        var description: String? = null,
)

object SharedContentConvertor {
    fun toEntity(sharedContent: SharedContent): SharedContentEntity {
        val entity = SharedContentEntity()
        entity.apply {
            fileUrl = sharedContent.fileUrl
            title = sharedContent.title
            description = sharedContent.description
        }
        return entity
    }

    fun toModel(sharedContentEntity: SharedContentEntity): SharedContent {
        val model = SharedContent()
        model.apply {
            fileUrl = sharedContentEntity.fileUrl
            title = sharedContentEntity.title
            description = sharedContentEntity.description
        }
        return model
    }
}

enum class EngagementType {
    OneOnOne, Webinar, AskMeAnything, Content
}