package com.example.newversity.services.teacher

import com.example.newversity.entity.Session
import com.example.newversity.model.SessionConvertor
import com.example.newversity.model.SessionModel
import com.example.newversity.repository.SessionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class SessionService(
        @Autowired val sessionRepository: SessionRepository
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
                return ResponseEntity.ok().body(SessionConvertor.toModel(sessionDetail))
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
                ResponseEntity.ok(upcomingSessionList.map { SessionConvertor.toModel(it) })
            }
            "previous" -> {
                ResponseEntity.ok(previousSessionList.map { SessionConvertor.toModel(it) })
            }
            else -> {
                ResponseEntity.ok(sessionList.map { SessionConvertor.toModel(it) })
            }
        }
    }

    fun getUpcomingSessionByTeacherId(teacherId: String): ResponseEntity<*> {
        val sessionList = sessionRepository.findAllByTeacherId(teacherId)
        val currentTime = Date()
        currentTime.time = System.currentTimeMillis() - 30 * 60 * 1000
        val upcomingSessionList = sessionList.filter {
            it.startDate!! > currentTime
        }
        return ResponseEntity.ok(upcomingSessionList.map { SessionConvertor.toModel(it) })
    }

    fun getPreviousSessionByTeacherId(teacherId: String): ResponseEntity<*> {
        val sessionList = sessionRepository.findAllByTeacherId(teacherId)
        val currentTime = Date()
        currentTime.time = System.currentTimeMillis() - 30 * 60 * 1000
        val upcomingSessionList = sessionList.filter {
            it.startDate!! <= currentTime
        }
        return ResponseEntity.ok(upcomingSessionList.map { SessionConvertor.toModel(it) })

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
        val sessionDetail = sessionRepository.save(session)
        return ResponseEntity.ok().body(SessionConvertor.toModel(sessionDetail))
    }

    fun isSessionModelValid(sessionModel: SessionModel): Boolean {
        return !sessionModel.teacherId.isNullOrEmpty() && !sessionModel.studentId.isNullOrEmpty()
                && sessionModel.amount != null && sessionModel.startDate != null &&
                !sessionModel.sessionType.isNullOrEmpty()
    }
}