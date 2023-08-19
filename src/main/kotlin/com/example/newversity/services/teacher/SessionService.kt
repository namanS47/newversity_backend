package com.example.newversity.services.teacher

import com.example.newversity.entity.teacher.Session
import com.example.newversity.model.NotificationDetailsModel
import com.example.newversity.model.SessionConvertor
import com.example.newversity.model.SessionModel
import com.example.newversity.model.student.SessionCountModel
import com.example.newversity.repository.SessionRepository
import com.example.newversity.repository.students.StudentRepository
import com.example.newversity.services.firebase.FirebaseMessagingService
import com.example.newversity.services.room.RoomService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class SessionService(
        @Autowired val sessionRepository: SessionRepository,
        @Autowired val studentRepository: StudentRepository,
        @Autowired val roomService: RoomService,
        @Autowired val teacherServices: TeacherServices,
        @Autowired val availabilityService: AvailabilityService,
        @Autowired val firebaseMessagingService: FirebaseMessagingService
) {

    fun getAllSessionCountByTeacherId(teacherId: String): ResponseEntity<*> {
        val sessionCount = sessionRepository.findAllByTeacherId(teacherId).count()
        val sessionCountModel = SessionCountModel()
        sessionCountModel.apply {
            totalSessionCount = sessionCount
        }
        return ResponseEntity.ok(sessionCountModel)
    }

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
                var sessionDetail = sessionRepository.save(SessionConvertor.toEntity(sessionModel))
                sessionDetail = roomService.sessionAuthTokenForRoom(sessionDetail)
                sessionModel.let { availabilityService.bookAvailability(it.availabilityId!!, it.startDate!!, it.endDate!!) }
                sessionDetail = sessionRepository.save(sessionDetail)

                //Send push notification
                //TODO: make this notification service async
                val notificationDetailsModel = NotificationDetailsModel()
                notificationDetailsModel.apply {
                    userIds = listOf(sessionModel.teacherId!!)
                    title = "Hii Mentor"
                    body = "A student has booked your session"
                }
                firebaseMessagingService.sendBulkNotification(notificationDetailsModel)

                return ResponseEntity.ok().body(SessionConvertor.toModel(sessionDetail, null, null))
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
        }.sortedBy { it.startDate }
        val previousSessionList = sessionList.filter {
            it.startDate!! <= currentTime
        }.sortedByDescending { it.startDate }

        return when (type) {
            "upcoming" -> {
                ResponseEntity.ok(upcomingSessionList.map {
                    val studentDetail = it.studentId?.let { it1 -> studentRepository.findByStudentId(it1) }
                    val teacherDetail = it.teacherId?.let { it1 -> teacherServices.getCompleteTeacherDetails(it1) }
                    SessionConvertor.toModel(it, teacherDetail, studentDetail?.orElse(null))
                })
            }
            "previous" -> {
                ResponseEntity.ok(previousSessionList.map {
                    val studentDetail = it.studentId?.let { it1 -> studentRepository.findByStudentId(it1) }
                    val teacherDetail = it.teacherId?.let { it1 -> teacherServices.getCompleteTeacherDetails(it1) }
                    SessionConvertor.toModel(it, teacherDetail, studentDetail?.orElse(null))
                })
            }
            else -> {
                ResponseEntity.ok(sessionList.map {
                    val studentDetail = it.studentId?.let { it1 -> studentRepository.findByStudentId(it1) }
                    val teacherDetail = it.teacherId?.let { it1 -> teacherServices.getCompleteTeacherDetails(it1) }
                    SessionConvertor.toModel(it, teacherDetail, studentDetail?.orElse(null))
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
        }.sortedBy { it.startDate }
        val previousSessionList = sessionList.filter {
            it.startDate!! <= currentTime
        }.sortedByDescending { it.startDate }
        return when (type) {
            "upcoming" -> {
                ResponseEntity.ok(upcomingSessionList.map {
                    val studentDetail = it.studentId?.let { it1 -> studentRepository.findByStudentId(it1) }
                    val teacherDetail = it.teacherId?.let { it1 -> teacherServices.getCompleteTeacherDetails(it1) }
                    SessionConvertor.toModel(it, teacherDetail, studentDetail?.orElse(null))
                })
            }
            "previous" -> {
                ResponseEntity.ok(previousSessionList.map {
                    val studentDetail = it.studentId?.let { it1 -> studentRepository.findByStudentId(it1) }
                    val teacherDetail = it.teacherId?.let { it1 -> teacherServices.getCompleteTeacherDetails(it1) }
                    SessionConvertor.toModel(it, teacherDetail, studentDetail?.orElse(null))
                })
            }
            else -> {
                ResponseEntity.ok(sessionList.map {
                    val studentDetail = it.studentId?.let { it1 -> studentRepository.findByStudentId(it1) }
                    val teacherDetail = it.teacherId?.let { it1 -> teacherServices.getCompleteTeacherDetails(it1) }
                    SessionConvertor.toModel(it, teacherDetail, studentDetail?.orElse(null))
                })
            }
        }
    }

    fun getSessionById(id: String): ResponseEntity<*> {
        val session = sessionRepository.findById(id)
        return if(session.isPresent) {
            val studentDetail = session.get().studentId?.let { studentRepository.findByStudentId(it) }
            val teacherDetail = session.get().teacherId?.let { teacherServices.getCompleteTeacherDetails(it) }
            ResponseEntity.ok().body(SessionConvertor.toModel(session.get(), teacherDetail, studentDetail?.orElse(null)))
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
        return ResponseEntity.ok().body(SessionConvertor.toModel(sessionDetail, null, null))
    }

    fun isSessionModelValid(sessionModel: SessionModel): Boolean {
        return !sessionModel.teacherId.isNullOrEmpty() && !sessionModel.studentId.isNullOrEmpty()
                && sessionModel.amount != null && sessionModel.startDate != null &&
                !sessionModel.sessionType.isNullOrEmpty()
    }
}