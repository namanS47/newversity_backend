package com.example.NewVersity.repository

import com.example.NewVersity.entity.Availability
import org.springframework.data.mongodb.repository.MongoRepository

interface AvailabilityRepository: MongoRepository<Availability, String>  {
    fun findAllByTeacherId(teacherId: String): List<Availability>
}