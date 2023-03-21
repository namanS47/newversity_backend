package com.example.NewVersity.repository

import com.example.NewVersity.entity.TeacherExperience
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface TeacherExperienceRepository : MongoRepository<TeacherExperience, String> {
    fun findAllByTeacherId(teacherId: String): List<TeacherExperience>
}