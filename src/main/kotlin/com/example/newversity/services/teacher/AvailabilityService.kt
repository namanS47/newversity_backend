package com.example.newversity.services.teacher

import com.example.newversity.entity.teacher.Availability
import com.example.newversity.model.teacher.AvailabilityConverter
import com.example.newversity.model.teacher.AvailabilityModel
import com.example.newversity.model.EmptyJsonResponse
import com.example.newversity.repository.teacher.AvailabilityRepository
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

    fun getAllAvailabilityByTeacherIdAndDateResponse(teacherId: String, date: Date?): ResponseEntity<*> {
        val allAvailability = availabilityRepository.findAllByTeacherId(teacherId)

        val availabilityList = if(date != null) {
            allAvailability.filter {
                isDateSame(it.startDate!!, date) && it.booked != true
            }.sortedBy { it.startDate }
        } else {
            allAvailability.filter {
                isDateInFuture(it.startDate!!) && it.booked != true
            }.sortedBy { it.startDate }
        }

        return ResponseEntity.ok(availabilityList.map { AvailabilityConverter.toModel(it) })
    }

    fun getAllAvailabilityByTeacherIdAndDate(teacherId: String): List<Availability> {
        val allAvailability = availabilityRepository.findAllByTeacherId(teacherId)

        val futureAvailability =  allAvailability.filter {
            isDateInFuture(it.startDate!!) && it.booked != true
        }.sortedBy { it.startDate }
        return futureAvailability
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

    fun bookAvailability(availabilityId: String, startDate: Date, endDate: Date) {
        val availability = availabilityRepository.findById(availabilityId)
        if(availability.isPresent) {
            val availabilityDetail = availability.get()
            availabilityDetail.booked = true
            availabilityRepository.save(availabilityDetail)

            //Add new availability with remaining time
            val bookedAvailabilityModel = AvailabilityConverter.toModel(availabilityDetail)
            val startDateDifference = startDate.time - bookedAvailabilityModel.startDate!!.time
            val endDateDifference = bookedAvailabilityModel.endDate!!.time - endDate.time

            if(startDateDifference/60000 >= 45) {
                val newAvailability = AvailabilityModel(
                        teacherId = bookedAvailabilityModel.teacherId,
                        startDate = bookedAvailabilityModel.startDate,
                        endDate = Date(startDate.time - 15*60000),
                        sessionType = bookedAvailabilityModel.sessionType,
                        booked = false
                )
                availabilityRepository.save(AvailabilityConverter.toEntity(newAvailability))
            }

            if(endDateDifference/60000 >= 45) {
                val newAvailability = AvailabilityModel(
                        teacherId = bookedAvailabilityModel.teacherId,
                        startDate = Date(endDate.time + 15*60000),
                        endDate = bookedAvailabilityModel.endDate,
                        sessionType = bookedAvailabilityModel.sessionType,
                        booked = false
                )
                availabilityRepository.save(AvailabilityConverter.toEntity(newAvailability))
            }

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
        return (availabilityModelOne.startDate!! >= availability.startDate && availabilityModelOne.startDate!! < availability.endDate) ||
                (availabilityModelOne.endDate!! <= availability.endDate && availabilityModelOne.endDate!! > availability.startDate)
                || (availabilityModelOne.startDate!! <= availability.startDate && availabilityModelOne.endDate!! >= availability.endDate)
    }

    fun isDateSame(d1: Date, d2: Date): Boolean {
        return d1.toInstant().atZone(ZoneId.of("Asia/Kolkata")).toLocalDate() == d2.toInstant().atZone(ZoneId.of("Asia/Kolkata")).toLocalDate()
                && isDateInFuture(d1)
    }

    fun isDateInFuture(date: Date): Boolean {
        val currentDate = Date()
        return date > currentDate
    }
}