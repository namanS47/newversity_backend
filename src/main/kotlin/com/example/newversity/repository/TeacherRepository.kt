package com.example.newversity.repository

import com.example.newversity.entity.TeacherDetails
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface TeacherRepository: MongoRepository<TeacherDetails, String> {
    fun findByName(id: String): Optional<TeacherDetails>
    fun findByTeacherId(id: String): Optional<TeacherDetails>
}