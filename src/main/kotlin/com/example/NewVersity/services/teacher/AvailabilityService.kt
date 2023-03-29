package com.example.NewVersity.services.teacher

import com.example.NewVersity.model.AvailabilityConverter
import com.example.NewVersity.model.AvailabilityModel
import com.example.NewVersity.model.AvailabilityRequestModel
import com.example.NewVersity.model.EmptyJsonResponse
import com.example.NewVersity.repository.AvailabilityRepository
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
        if (availabilityRequestModel.teacherId.isNullOrEmpty() || availabilityRequestModel.date == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("status" to "Incomplete details"))
        }
        val allAvailability = availabilityRepository.findAllByTeacherId(availabilityRequestModel.teacherId!!)
        val availabilityList = allAvailability.filter {
            isDateSame(it.startDate!!, availabilityRequestModel.date!!)
        }
        return ResponseEntity.ok(availabilityList.map { AvailabilityConverter.toModel(it) })
    }

    fun addAvailability(availabilityList: List<AvailabilityModel>?): ResponseEntity<*> {
        availabilityList?.forEach { availabilityModel ->
            if (!isAvailabilityModelValid(availabilityModel)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("status" to "Incomplete details"))
            }

            availabilityModel.teacherId?.let { availabilityRepository.findAllByTeacherId(it) }?.forEach {
                if ((availabilityModel.startDate!! >= it.startDate && availabilityModel.startDate!! <= it.endDate) ||
                        (availabilityModel.endDate!! <= it.endDate && availabilityModel.endDate!! >= it.startDate)
                        || (availabilityModel.startDate!! <= it.startDate && availabilityModel.endDate!! > it.endDate)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("status" to "Overlapping duration, you already have availability within this duration"))
                }
            }
        }

        availabilityList?.forEach { availabilityModel ->
            availabilityRepository.save(AvailabilityConverter.toEntity(availabilityModel))
        }

        return ResponseEntity.ok(EmptyJsonResponse())
    }

    fun removeAvailability(id: String): ResponseEntity<*> {
        availabilityRepository.deleteById(id)
        return ResponseEntity.ok(EmptyJsonResponse())
    }

    fun isAvailabilityModelValid(availabilityModel: AvailabilityModel): Boolean {
        return availabilityModel.startDate != null && availabilityModel.endDate != null && !availabilityModel.teacherId.isNullOrEmpty()
    }

    fun isDateSame(d1: Date, d2: Date): Boolean {
        return d1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() == d2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }
}