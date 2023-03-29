package com.example.newversity.entity

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.util.*

@Document(collection = "teacher_education")
class TeacherEducation(
        @Field("teacher_id")
        var teacherId: String? = null,

        var name: String? = null,

        var degree: String? = null,

        @Field("start_date")
        var startDate: Date? = null,

        @Field("end_date")
        var endDate: Date? = null,

        @Field("currently_working_here")
        var currentlyWorkingHere: Boolean? = null,

        var grade: String? = null,
): AppEntity()