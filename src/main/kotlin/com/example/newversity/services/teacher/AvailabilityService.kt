package com.example.newversity.services.teacher

import com.example.newversity.entity.Availability
import com.example.newversity.model.AvailabilityConverter
import com.example.newversity.model.AvailabilityModel
import com.example.newversity.model.AvailabilityRequestModel
import com.example.newversity.model.EmptyJsonResponse
import com.example.newversity.repository.AvailabilityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.util.Date

@Service
class AvailabilityService(
        @Autowired val availabilityRepository: AvailabilityRepository
) {
    fun getAllAvailabilityByTeacherId(teacherId: String): ResponseEntity<*> {
        val allAvailability = availabilityRepository.findAllByTeacherId(teacherId)
        return ResponseEntity.ok(allAvailability.map { AvailabilityConverter.toModel(it) })
    }

    fun getAllAvailabilityByTeacherIdAndDate(availabilityRequestModel: AvailabilityRequestModel): ResponseEntity<*> {
        if (availabilityRequestModel.teacherId.isNullOrEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("status" to "teacherId missing"))
        }
        val allAvailability = availabilityRepository.findAllByTeacherId(availabilityRequestModel.teacherId!!)

        val availabilityList = if(availabilityRequestModel.date != null) {
            allAvailability.filter {
                isDateSame(it.startDate!!, availabilityRequestModel.date!!) && it.booked != true
            }.sortedBy { it.startDate }
        } else {
            allAvailability.filter {
                isDateInFuture(it.startDate!!) && it.booked != true
            }.sortedBy { it.startDate }
        }

        return ResponseEntity.ok(availabilityList.map { AvailabilityConverter.toModel(it) })
    }


    fun addAvailability(availabilityList: List<AvailabilityModel>?): ResponseEntity<*> {
        availabilityList?.forEach { availabilityModel ->
            if (!isAvailabilityModelValid(availabilityModel)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("status" to "Incomplete details"))
            }

            availabilityModel.teacherId?.let { availabilityRepository.findAllByTeacherId(it) }?.forEach {

                    if(availabilityModel.id != it.id) {
                        if (isAvailabilityOverlapping(availabilityModel, it)) {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("status" to "Overlapping duration, you already have availability within this duration"))
                        }
                    }
            }
        }

        availabilityList?.forEach { availabilityModel ->
            availabilityRepository.save(AvailabilityConverter.toEntity(availabilityModel))
        }

        return ResponseEntity.ok(EmptyJsonResponse())
    }

    fun bookAvailability(availabilityId: String) {
        val availability = availabilityRepository.findById(availabilityId)
        if(availability.isPresent) {
            val availabilityDetail = availability.get()
            availabilityDetail.booked = true
            availabilityRepository.save(availabilityDetail)
        }
    }

    fun removeAvailability(id: String): ResponseEntity<*> {
        availabilityRepository.deleteById(id)
        return ResponseEntity.ok(EmptyJsonResponse())
    }

    fun isAvailabilityModelValid(availabilityModel: AvailabilityModel): Boolean {
        return availabilityModel.startDate != null && availabilityModel.endDate != null && !availabilityModel.teacherId.isNullOrEmpty()
    }

    fun isAvailabilityOverlapping(availabilityModelOne: AvailabilityModel, availability: Availability) :Boolean {
        return (availabilityModelOne.startDate!! >= availability.startDate && availabilityModelOne.startDate!! <= availability.endDate) ||
                (availabilityModelOne.endDate!! <= availability.endDate && availabilityModelOne.endDate!! >= availability.startDate)
                || (availabilityModelOne.startDate!! <= availability.startDate && availabilityModelOne.endDate!! > availability.endDate)
    }

    fun isDateSame(d1: Date, d2: Date): Boolean {
        return d1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() == d2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                && isDateInFuture(d1)
    }

    fun isDateInFuture(date: Date): Boolean {
        val currentDate = Date()
        return date > currentDate
    }
}