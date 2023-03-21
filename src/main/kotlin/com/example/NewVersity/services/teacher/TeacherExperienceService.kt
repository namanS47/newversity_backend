package com.example.NewVersity.services.teacher

import com.example.NewVersity.entity.TeacherExperience
import com.example.NewVersity.model.TeacherExperienceConverter
import com.example.NewVersity.model.TeacherExperienceModel
import com.example.NewVersity.repository.TeacherExperienceRepository
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

    fun getAllExperienceByTeacherId(teacherId: String): ResponseEntity<*> {
        val teacherExperiences = teacherExperienceRepository.findAllByTeacherId(teacherId)
        return ResponseEntity.ok(teacherExperiences.map { TeacherExperienceConverter.toModel(it) })
    }
}