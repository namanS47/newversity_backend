package com.example.newversity.services.student

import com.example.newversity.model.NotificationDetailsModel
import com.example.newversity.model.common.RequestSessionModel
import com.example.newversity.model.common.RequestSessionModelConvertor
import com.example.newversity.repository.students.RequestSessionRepository
import com.example.newversity.services.firebase.FirebaseMessagingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class RequestSessionService(
        @Autowired val requestSessionRepository: RequestSessionRepository,
        @Autowired val firebaseMessagingService: FirebaseMessagingService
) {
    fun addSessionRequest(requestSessionModel: RequestSessionModel): ResponseEntity<*> {
        if(requestSessionModel.forCreators == true) {
            //Send push notification
            //TODO: make this notification service async
            val notificationDetailsModel = NotificationDetailsModel()
            notificationDetailsModel.apply {
                userIds = listOf(requestSessionModel.teacherId!!)
                title = "A student wants to book your session, please add your availability"
                body = requestSessionModel.agenda
            }
            firebaseMessagingService.sendBulkNotification(notificationDetailsModel)
        }

        requestSessionRepository.save(RequestSessionModelConvertor.toEntity(requestSessionModel))
        return ResponseEntity.ok().body(mapOf("success" to "true"))
    }
}