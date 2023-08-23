package com.example.newversity.repository.teacher

import com.example.newversity.entity.teacher.TeacherDetails
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface TeacherRepository: MongoRepository<TeacherDetails, String> {
    fun findByName(id: String): Optional<TeacherDetails>
    fun findByTeacherId(id: String): Optional<TeacherDetails>
    fun findByUserName(id: String): Optional<TeacherDetails>
}