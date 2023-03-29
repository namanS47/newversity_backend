package com.example.newversity.entity

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.util.Date

@Document(collection = "availability")
class Availability (
        @Field("teacher_id")
        var teacherId                   :String?                        = null,

        @Field("start_date")
        var startDate: Date? = null,

        @Field("end_date")
        var endDate: Date? = null,

        @Field("session_type")
        var sessionType: String? = null,

        var booked: Boolean? = null,

        @Field("total_booked")
        var totalBooked: Int? = null,
) : AppEntity()