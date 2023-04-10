package com.example.newversity.repository.students

import com.example.newversity.entity.students.Student
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface StudentRepository: MongoRepository<Student, String> {
    fun findByStudentId(studentId: String): Optional<Student>
}