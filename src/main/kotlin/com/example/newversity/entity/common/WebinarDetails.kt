package com.example.newversity.entity.common

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field


@Document(collection = "webinar")
class WebinarDetails (
    @Field("teacher_id")
    var teacherId                   :String?                        = null,

    @Field("student_id")
    var studentId                   :String?                        = null,

    var title: String? = null,

    @Field("feedback")
    var feedback: String? = null,

    @Field("rating")
    var rating: Double? = null,

    var agenda: String? = null,

    @Field("issue_raised")
    var issueRaised: String? = null,
)