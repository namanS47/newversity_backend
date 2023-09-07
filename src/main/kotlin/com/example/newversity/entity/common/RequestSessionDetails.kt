package com.example.newversity.entity.common

import com.example.newversity.entity.teacher.AppEntity
import com.example.newversity.model.common.EngagementType
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "request_session")
class RequestSessionDetails(
        @Field("teacher_id")
        var teacherId: String? = null,

        @Field("student_id")
        var studentId: String? = null,

        @Field("engagement_type")
        var engagementType: List<EngagementType>? = null,

        var info: String? = null,

        var agenda: String? = null,

        var location: String? = null,

        @Field("for_creators")
        var forCreators: Boolean? = null,
) : AppEntity()