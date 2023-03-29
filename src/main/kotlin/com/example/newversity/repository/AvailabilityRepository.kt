package com.example.newversity.repository

import com.example.newversity.entity.Availability
import org.springframework.data.mongodb.repository.MongoRepository

interface AvailabilityRepository: MongoRepository<Availability, String>  {
    fun findAllByTeacherId(teacherId: String): List<Availability>
}