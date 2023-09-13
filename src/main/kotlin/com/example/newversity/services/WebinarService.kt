package com.example.newversity.services

import com.example.newversity.model.StudentInfoModel
import com.example.newversity.model.StudentInfoModelConvertor
import com.example.newversity.model.WebinarConvertor
import com.example.newversity.model.WebinarModel
import com.example.newversity.repository.WebinarRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class WebinarService(
        @Autowired val webinarRepository: WebinarRepository
) {
    fun addWebinar(webinarModel: WebinarModel): ResponseEntity<*> {
        if(!webinarModel.id.isNullOrEmpty()) {
            val webinarEntity = webinarRepository.findById(webinarModel.id!!)
            if(webinarEntity.isPresent) {
                val webinar = webinarEntity.get()
                webinar.apply {
                    webinarModel.teacherId?.let {
                        teacherId = it
                    }
                    webinarModel.teacherName?.let {
                        teacherName = it
                    }
                    webinarModel.teacherTitle?.let {
                        teacherTitle = it
                    }
                    webinarModel.teacherProfilePicture?.let {
                        teacherProfilePicture = it
                    }
                    webinarModel.webinarDate?.let {
                        webinarDate = it
                    }
                    webinarModel.title?.let {
                        title = it
                    }
                    webinarModel.feedback?.let {
                        feedback = it
                    }
                    webinarModel.shareLink?.let {
                        shareLink = it
                    }
                    webinarModel.joiningLink?.let {
                        joiningLink = it
                    }
                }
                webinarRepository.save(webinar)
                return ResponseEntity.ok().body(WebinarConvertor.toModel(webinar))
            } else {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("status" to "webinar id doesn't exist"))
            }
        }
        val webinar = webinarRepository.save(WebinarConvertor.toEntity(webinarModel))
        return ResponseEntity.ok().body(webinar)
    }

    fun getAllFutureWebinar(): ResponseEntity<*> {
        val webinarList = webinarRepository.findAll()
        val result = webinarList.filter {
            it.webinarDate != null && it.webinarDate!! > Date()
        }
        return ResponseEntity.ok().body(result.map { WebinarConvertor.toModel(it) })
    }

    fun getWebinarDetails(webinarId: String): ResponseEntity<*> {
        val webinar = webinarRepository.findById(webinarId)
        return if (webinar.isPresent) {
            val webinarEntity = webinar.get()
            ResponseEntity.ok().body(WebinarConvertor.toModel(webinarEntity))
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("status" to "webinar doesn't exist"))
        }
    }

    fun registerInWebinar(studentInfoModel: StudentInfoModel, webinarId: String): ResponseEntity<*> {
        val webinar = webinarRepository.findById(webinarId)
        if (webinar.isPresent) {
            val webinarEntity = webinar.get()
            var studentAlreadyRegistered = false
            webinarEntity.studentsInfoList?.forEach {
                if (it.studentId == studentInfoModel.studentId) {
                    studentAlreadyRegistered = true
                }
            }

            return if (studentAlreadyRegistered) {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("status" to "already registered"))
            } else {
                val studentInfoList = webinarEntity.studentsInfoList ?: arrayListOf()
                studentInfoList.add(StudentInfoModelConvertor.toEntity(studentInfoModel))
                webinarEntity.studentsInfoList = studentInfoList
                webinarRepository.save(webinarEntity)
                ResponseEntity.ok().body(mapOf("status" to "success"))
            }

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("status" to "webinar id doesn't exist"))
        }
    }

    fun addWebinarReview(studentInfoModel: StudentInfoModel, webinarId: String): ResponseEntity<*> {
        val webinar = webinarRepository.findById(webinarId)
        if (webinar.isPresent) {
            val webinarEntity = webinar.get()
            var reviewAdded = false
            webinarEntity.studentsInfoList?.forEach {
                if (it.studentId == studentInfoModel.studentId) {
                    it.review = studentInfoModel.review
                    reviewAdded = true
                }
            }

            return if(reviewAdded) {
                webinarRepository.save(webinarEntity)
                ResponseEntity.ok().body(mapOf("status" to "success"))
            } else {
                val studentInfoList = webinarEntity.studentsInfoList ?: arrayListOf()
                studentInfoList.add(StudentInfoModelConvertor.toEntity(studentInfoModel))
                webinarEntity.studentsInfoList = studentInfoList
                webinarRepository.save(webinarEntity)
                ResponseEntity.ok().body(mapOf("status" to "success"))
            }

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("status" to "webinar id doesn't exist"))
        }
    }
}