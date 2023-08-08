package com.example.newversity.model

import com.example.newversity.entity.teacher.Availability
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.*

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class AvailabilityRequestModel(
        var teacherId: String? = null,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "IST")
        var date: Date? = null,
)

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class AvailabilityListModel(
        var availabilityList: List<AvailabilityModel>?
)

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class AvailabilityModel(
        var id: String? = null,
        var teacherId: String? = null,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "IST")
        var startDate: Date? = null,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "IST")
        var endDate: Date? = null,
        var sessionType: String? = null,
        var booked: Boolean? = null,
        var totalBooked: Int? = null,
)

object AvailabilityConverter {
    fun toEntity(availabilityModel: AvailabilityModel): Availability {
        val entity = Availability()
        entity.apply {
            id = availabilityModel.id
            teacherId = availabilityModel.teacherId
            startDate = availabilityModel.startDate
            endDate = availabilityModel.endDate
            sessionType = availabilityModel.sessionType
            booked = availabilityModel.booked
            totalBooked = availabilityModel.totalBooked
        }
        return entity
    }

    fun toModel(availability: Availability): AvailabilityModel {
        val model = AvailabilityModel()
        model.apply {
            id = availability.id
            teacherId = availability.teacherId
            startDate = availability.startDate
            endDate = availability.endDate
            sessionType = availability.sessionType
            booked = availability.booked
            totalBooked = availability.totalBooked
        }
        return model
    }
}