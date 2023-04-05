package com.example.newversity.services.teacher

import com.example.newversity.entity.TeacherExperience
import com.example.newversity.model.TeacherExperienceConverter
import com.example.newversity.model.TeacherExperienceModel
import com.example.newversity.repository.TeacherExperienceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class TeacherExperienceService(
        @Autowired val teacherExperienceRepository: TeacherExperienceRepository
) {
    fun getAllTeacherExperience(teacherId: String) : ResponseEntity<*> {
        val experiences = teacherExperienceRepository.findAllByTeacherId(teacherId)
        return ResponseEntity.ok(experiences.map { TeacherExperienceConverter.toModel(it) })
    }

    fun addTeacherExperience(experience: TeacherExperienceModel): ResponseEntity<*> {
        teacherExperienceRepository.save(TeacherExperienceConverter.toEntity(experience))
        return ResponseEntity.ok(true)
    }

    fun getAllExperienceListByTeacherId(teacherId: String): List<TeacherExperience> {
        return teacherExperienceRepository.findAllByTeacherId(teacherId)
    }
}