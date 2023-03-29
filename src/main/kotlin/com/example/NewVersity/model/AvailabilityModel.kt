package com.example.NewVersity.model

import com.example.NewVersity.entity.Availability
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.*

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
class AvailabilityRequestModel(
        var teacherId: String? = null,
        var date: Date? = null,
)

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
class AvailabilityListModel(
        var availabilityList: List<AvailabilityModel>?
)

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
class AvailabilityModel(
        var id: String? = null,
        var teacherId: String? = null,
        var startDate: Date? = null,
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