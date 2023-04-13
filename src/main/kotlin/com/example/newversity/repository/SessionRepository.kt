package com.example.newversity.repository

import com.example.newversity.entity.Session
import org.springframework.data.mongodb.repository.MongoRepository

interface SessionRepository : MongoRepository<Session, String> {
    fun findAllByTeacherId(teacherId: String) : List<Session>
    fun findAllByStudentId(studentId: String) : List<Session>
}