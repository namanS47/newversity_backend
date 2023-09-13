package com.example.newversity.entity.common

import com.example.newversity.entity.teacher.AppEntity
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.util.*


@Document(collection = "webinar")
class WebinarDetails(
        @Field("teacher_id")
        var teacherId: String? = null,

        @Field("teacher_name")
        var teacherName: String? = null,

        @Field("teacher_title")
        var teacherTitle: String? = null,

        @Field("teacher_profile_picture")
        var teacherProfilePicture: String? = null,

        @Field("student_info_list")
        var studentsInfoList: ArrayList<StudentInfo>? = null,

        @Field("webinar_date")
        var webinarDate: Date? = null,

        var title: String? = null,

        @Field("feedback")
        var feedback: String? = null,

        @Field("rating")
        var rating: Double? = null,

        var agenda: String? = null,

        @Field("issue_raised")
        var issueRaised: String? = null,

        @Field("share_link")
        var shareLink: String? = null,

        @Field("joining_link")
        var joiningLink: String? = null,
): AppEntity()

class StudentInfo (
        @Field("student_id")
        var studentId: String? = null,

        var info: String? = null,

        var agenda: String? = null,

        var location: String? = null,

        var review: String? = null,
)


