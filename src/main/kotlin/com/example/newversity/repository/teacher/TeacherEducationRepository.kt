package com.example.newversity.repository.teacher

import com.example.newversity.entity.teacher.TeacherEducation
import org.springframework.data.mongodb.repository.MongoRepository

interface TeacherEducationRepository : MongoRepository<TeacherEducation, String> {
    fun findAllByTeacherId(teacherId: String): List<TeacherEducation>
}