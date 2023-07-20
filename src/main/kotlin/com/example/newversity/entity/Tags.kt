package com.example.newversity.entity

import com.example.newversity.services.teacher.TagStatus
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "tags")
class Tags (
        @Indexed
        @Field("tag_name")
        var tagName: String? = null,

        @Field("teacher_tag_detail_list")
        var teacherTagDetailList: MutableMap<String, TeacherTagDetails>? = null,

        @Field("tag_category")
        var tagCategory: String? = null,

        @Field("admin_approve")
        var adminApprove: Boolean? = null
) : AppEntity()

class TeacherTagDetails(
        @Field("tag_status")
        var tagStatus: TagStatus? = null,

        @Field("documents")
        var documents: ArrayList<String>? = null,

        var reason: String? = null,

        var suggestion: String? = null,
)