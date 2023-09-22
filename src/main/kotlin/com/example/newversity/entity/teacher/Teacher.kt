package com.example.newversity.entity.teacher

import com.example.newversity.model.TagModel
import com.example.newversity.model.teacher.EngagementType
import com.example.newversity.model.teacher.SharedContent
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Document(collection = "teacher")
class TeacherDetails(
        @Field("user_id")
        var userId: String? = null,

        @Field("teacher_id")
        var teacherId: String? = null,

        var name: String? = null,

        @Field("mobile_number")
        var mobileNumber: String? = null,

        var email: String? = null,

        @Field("intro_video_url")
        var introVideoUrl: String? = null,

        var location: String? = null,

        var gender: String? = null,

        var age: String? = null,

        var title: String? = null,

        var info: String? = null,

        @Field("uploaded_documents")
        var uploadedDocuments: List<String>? = null,

        var tags: List<TagModel>? = null,

        var education: String? = null,

        var designation: String? = null,

        @Field("profile_url")
        var profileUrl: String? = null,

        @Field("profile_picture_url")
        var profilePictureUrl: String? = null,

        @Field("session_pricing")
        var sessionPricing: HashMap<String, Double>? = null,

        var language: List<String>? = null,

        @Field("is_new")
        var isNew: Boolean? = null,

        @Field("is_approved")
        var isApproved: Boolean? = null,

        var level: Int? = null,

        @Field("user_name")
        var userName: String? = null,

        @Field("engagement_type")
        var engagementType: List<EngagementType>? = null,

        @Field("shared_content")
        var sharedContent: ArrayList<SharedContentEntity>? = null,
) : AppEntity()

class SharedContentEntity (
        @Field("file_url")
        var fileUrl: List<String>? = null,

        var title: String? = null,

        var description: String? = null,
)
