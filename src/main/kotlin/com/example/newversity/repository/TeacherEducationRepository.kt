package com.example.newversity.repository

import com.example.newversity.entity.TeacherEducation
import org.springframework.data.mongodb.repository.MongoRepository

interface TeacherEducationRepository : MongoRepository<TeacherEducation, String> {
    fun findAllByTeacherId(teacherId: String): List<TeacherEducation>
}