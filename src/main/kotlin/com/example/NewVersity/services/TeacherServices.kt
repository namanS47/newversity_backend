package com.example.NewVersity.services

import com.example.NewVersity.model.TeacherConverter
import com.example.NewVersity.model.TeacherDetailModel
import com.example.NewVersity.repository.TeacherRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class TeacherServices(
        @Autowired val teacherRepository: TeacherRepository,
        @Autowired val tagsService: TagsService
) {
    fun save(teacherDetailModel: TeacherDetailModel): ResponseEntity<*> {
        return if(isTeacherValid(teacherDetailModel)) {
            if(!teacherRepository.findByTeacherId(teacherDetailModel.teacherId ?: "").isPresent){
                val teacherDetails = teacherRepository.save(TeacherConverter.toEntity(teacherDetailModel))
                //TODO: Make below line run on separate thread
                tagsService.updateTagList(teacherDetailModel.tags ?: arrayListOf(), arrayListOf(), teacherDetailModel.teacherId ?: "")
//                tagsService.testing()
                ResponseEntity.ok(TeacherConverter.toModel(teacherDetails))
            } else {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("status" to "Teacher Already Exist"))
            }
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("status" to "Missing Necessary Details"))
        }
    }

    fun getTeacher(teacherId: String) : ResponseEntity<*> {
        val teacherDetails = teacherRepository.findByTeacherId(teacherId)
        return if(teacherDetails.isPresent) {
            ResponseEntity.ok(TeacherConverter.toModel(teacherDetails.get()))
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("status" to "Teacher Doesn't Exist"))
        }
    }

    fun updateTeacher(teacherDetailModel: TeacherDetailModel, teacherId: String): ResponseEntity<Boolean> {
        val teacherDetailsEntity = teacherRepository.findByTeacherId(teacherId)
        if(teacherDetailsEntity.isPresent) {
            var teacher = teacherDetailsEntity.get()
            teacherDetailModel.introVideoUrl?.let {
                teacher.introVideoUrl = it
            }
            teacherDetailModel.location?.let {
                teacher.location = it
            }
            teacherDetailModel.title?.let {
                teacher.title = it
            }
            teacherDetailModel.info?.let {
                teacher.info = it
            }
            teacherDetailModel.tags?.let {
                tagsService.updateTagList(it, teacher.tags ?: arrayListOf(), teacher.teacherId ?: "")
                teacher.tags = it
            }
            teacherDetailModel.education?.let {
                teacher.education = it
            }
            teacherDetailModel.designation?.let {
                teacher.designation = it
            }
            teacherDetailModel.profileUrl?.let {
                teacher.profileUrl = it
            }
            teacherDetailModel.profilePictureUrl?.let {
                teacher.profilePictureUrl = it
            }
            teacherDetailModel.sessionPricing?.let {
                teacher.sessionPricing = it
            }
            teacherDetailModel.language?.let {
                teacher.language = it
            }
            teacher = teacherRepository.save(teacher)
            return ResponseEntity.ok(true)
        } else {
            return ResponseEntity.ok(false)
        }
    }

    fun isTeacherValid(teacherDetailModel: TeacherDetailModel): Boolean {
        var isTeacherValid = true
        teacherDetailModel.let {
            isTeacherValid =  !(it.teacherId.isNullOrEmpty() || it.mobileNumber.isNullOrEmpty() || it.email.isNullOrEmpty() || it.name.isNullOrEmpty())
        }
        return isTeacherValid
    }
}