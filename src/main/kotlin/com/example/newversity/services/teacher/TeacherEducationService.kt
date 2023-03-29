package com.example.newversity.services.teacher

import com.example.newversity.model.TeacherEducationConvertor
import com.example.newversity.model.TeacherEducationModel
import com.example.newversity.repository.TeacherEducationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class TeacherEducationService(
        @Autowired val teacherEducationRepository: TeacherEducationRepository
) {
    fun getAllTeacherEducationDetails(teacherId: String) : ResponseEntity<*> {
        val educationDetails = teacherEducationRepository.findAllByTeacherId(teacherId)
        return ResponseEntity.ok(educationDetails.map { TeacherEducationConvertor.toModel(it) })
    }

    fun addTeacherEducationDetails(educationModel: TeacherEducationModel): ResponseEntity<*> {
        teacherEducationRepository.save(TeacherEducationConvertor.toEntity(educationModel))
        return ResponseEntity.ok(true)
    }
}