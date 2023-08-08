package com.example.newversity.repository

import com.example.newversity.entity.teacher.TeacherExperience
import org.springframework.data.mongodb.repository.MongoRepository

interface TeacherExperienceRepository : MongoRepository<TeacherExperience, String> {
    fun findAllByTeacherId(teacherId: String): List<TeacherExperience>
}