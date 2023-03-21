package com.example.NewVersity.entity

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.util.*

@Document(collection = "teacher_experience")
class TeacherExperience(
        var teacherId: String? = null,

        var title: String? = null,

        @Field("employment_type")
        var employmentType: String? = null,

        @Field("company_name")
        var companyName: String? = null,

        var location: String? = null,

        @Field("start_date")
        var startDate: Date? = null,

        @Field("end_date")
        var endDate: Date? = null,

        @Field("currently_working_here")
        var currentlyWorkingHere: Boolean? = null,
): AppEntity()