package com.example.newversity.services.teacher

import com.example.newversity.entity.Session
import com.example.newversity.model.SessionConvertor
import com.example.newversity.model.SessionModel
import com.example.newversity.repository.SessionRepository
import com.example.newversity.repository.TeacherRepository
import com.example.newversity.repository.students.StudentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class SessionService(
        @Autowired val sessionRepository: SessionRepository,
        @Autowired val studentRepository: StudentRepository,
        @Autowired val teacherRepository: TeacherRepository,
        @Autowired val tagService: TagsService
) {

    fun addSession(sessionModel: SessionModel): ResponseEntity<*> {
        sessionModel.id?.let {
            val sessionDetailEntity = sessionRepository.findById(it)
            if (sessionDetailEntity.isPresent) {
                return updateSessionDetails(sessionDetailEntity.get(), sessionModel)
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("status" to "Session with this id doesn't exist"))
            }
        } ?: run {
            if (isSessionModelValid(sessionModel)) {
                val sessionDetail = sessionRepository.save(SessionConvertor.toEntity(sessionModel))
                return ResponseEntity.ok().body(SessionConvertor.toModel(sessionDetail, null, null, null))
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("status" to "Incomplete details"))
            }
        }
    }

    fun getSessionByTeacherId(teacherId: String, type: String): ResponseEntity<*> {
        val sessionList = sessionRepository.findAllByTeacherId(teacherId)
        val currentTime = Date()
        currentTime.time = System.currentTimeMillis() - 30 * 60 * 1000
        val upcomingSessionList = sessionList.filter {
            it.startDate!! > currentTime
        }
        val previousSessionList = sessionList.filter {
            it.startDate!! <= currentTime
        }

        return when (type) {
            "upcoming" -> {
                ResponseEntity.ok(upcomingSessionList.map {
                    val studentDetail = it.studentId?.let { it1 -> studentRepository.findByStudentId(it1) }
                    val teacherDetail = it.teacherId?.let { it1 -> teacherRepository.findByTeacherId(it1) }
                    val tagList = tagService.getAllTagsModelWithTeacherId(teacherId)
                    SessionConvertor.toModel(it, teacherDetail?.orElse(null), studentDetail?.orElse(null), tagList)
                })
            }
            "previous" -> {
                ResponseEntity.ok(previousSessionList.map {
                    val studentDetail = it.studentId?.let { it1 -> studentRepository.findByStudentId(it1) }
                    val teacherDetail = it.teacherId?.let { it1 -> teacherRepository.findByTeacherId(it1) }
                    val tagList = tagService.getAllTagsModelWithTeacherId(teacherId)
                    SessionConvertor.toModel(it, teacherDetail?.orElse(null), studentDetail?.orElse(null), tagList)
                })
            }
            else -> {
                ResponseEntity.ok(sessionList.map {
                    val studentDetail = it.studentId?.let { it1 -> studentRepository.findByStudentId(it1) }
                    val teacherDetail = it.teacherId?.let { it1 -> teacherRepository.findByTeacherId(it1) }
                    val tagList = tagService.getAllTagsModelWithTeacherId(teacherId)
                    SessionConvertor.toModel(it, teacherDetail?.orElse(null), studentDetail?.orElse(null), tagList)
                })
            }
        }
    }

    fun getSessionByStudentId(studentId: String, type: String): ResponseEntity<*> {
        val sessionList = sessionRepository.findAllByStudentId(studentId)
        val currentTime = Date()
        currentTime.time = System.currentTimeMillis() - 30 * 60 * 1000
        val upcomingSessionList = sessionList.filter {
            it.startDate!! > currentTime
        }
        val previousSessionList = sessionList.filter {
            it.startDate!! <= currentTime
        }
        return when (type) {
            "upcoming" -> {
                ResponseEntity.ok(upcomingSessionList.map {
                    val studentDetail = it.studentId?.let { it1 -> studentRepository.findByStudentId(it1) }
                    val teacherDetail = it.teacherId?.let { it1 -> teacherRepository.findByTeacherId(it1) }
                    val tagList = it.teacherId?.let { it1 -> tagService.getAllTagsModelWithTeacherId(it1) }
                    SessionConvertor.toModel(it, teacherDetail?.orElse(null), studentDetail?.orElse(null), tagList)

                })
            }
            "previous" -> {
                ResponseEntity.ok(previousSessionList.map {
                    val studentDetail = it.studentId?.let { it1 -> studentRepository.findByStudentId(it1) }
                    val teacherDetail = it.teacherId?.let { it1 -> teacherRepository.findByTeacherId(it1) }
                    val tagList = it.teacherId?.let { it1 -> tagService.getAllTagsModelWithTeacherId(it1) }
                    SessionConvertor.toModel(it, teacherDetail?.orElse(null), studentDetail?.orElse(null), tagList)

                })
            }
            else -> {
                ResponseEntity.ok(sessionList.map {
                    val studentDetail = it.studentId?.let { it1 -> studentRepository.findByStudentId(it1) }
                    val teacherDetail = it.teacherId?.let { it1 -> teacherRepository.findByTeacherId(it1) }
                    val tagList = it.teacherId?.let { it1 -> tagService.getAllTagsModelWithTeacherId(it1) }
                    SessionConvertor.toModel(it, teacherDetail?.orElse(null), studentDetail?.orElse(null), tagList)
                })
            }
        }
    }

    fun getSessionById(id: String): ResponseEntity<*> {
        val session = sessionRepository.findById(id)
        return if(session.isPresent) {
            val studentDetail = session.get().studentId?.let { studentRepository.findByStudentId(it) }
            val teacherDetail = session.get().teacherId?.let { teacherRepository.findByTeacherId(it) }
            val tagList = session.get().teacherId?.let { tagService.getAllTagsModelWithTeacherId(it) }
            ResponseEntity.ok().body(SessionConvertor.toModel(session.get(), teacherDetail?.orElse(null), studentDetail?.orElse(null), tagList))
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("status" to "Session Id doesn't exist"))
        }
    }

    fun updateSessionDetails(session: Session, sessionModel: SessionModel): ResponseEntity<*> {
        sessionModel.agenda?.let {
            session.agenda = it
        }

        sessionModel.paymentId?.let {
            session.paymentId = it
        }

        sessionModel.mentorNote?.let {
            session.mentorNote = it
        }

        sessionModel.studentFeedback?.let {
            session.studentFeedback = it
        }

        sessionModel.studentRating?.let {
            session.studentRating = it
        }

        sessionModel.issueRaised?.let {
            session.issueRaised = it
        }

        sessionModel.cancelled?.let {
            session.cancelled = it
        }
        val sessionDetail = sessionRepository.save(session)
        return ResponseEntity.ok().body(SessionConvertor.toModel(sessionDetail, null, null, null))
    }

    fun isSessionModelValid(sessionModel: SessionModel): Boolean {
        return !sessionModel.teacherId.isNullOrEmpty() && !sessionModel.studentId.isNullOrEmpty()
                && sessionModel.amount != null && sessionModel.startDate != null &&
                !sessionModel.sessionType.isNullOrEmpty()
    }
}