package com.example.NewVersity.repository

import com.example.NewVersity.entity.TeacherEducation
import org.springframework.data.mongodb.repository.MongoRepository

interface TeacherEducationRepository : MongoRepository<TeacherEducation, String> {
    fun findAllByTeacherId(teacherId: String): List<TeacherEducation>
}