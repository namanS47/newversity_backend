package com.example.newversity.services.firebase

import com.example.newversity.model.NotificationDetailsModel
import com.example.newversity.repository.CommonDetailsRepository
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service


@Service
class FirebaseMessagingService(
        @Autowired val commonDetailsRepository: CommonDetailsRepository
) {
    fun sendProfileAndAvailabilityNotificationToMentors() {
        
    }

    @Throws(FirebaseMessagingException::class)
    @Async
    fun sendNotification(notificationDetails: NotificationDetailsModel) {
        val fcmToken = notificationDetails.userId?.let { getFCMToken(it) }
        if(!fcmToken.isNullOrEmpty()) {
            val notification: Notification = Notification
                    .builder()
                    .setTitle(notificationDetails.title)
                    .setBody(notificationDetails.body)
                    .build()
            val message: Message = Message
                    .builder()
                    .setToken(fcmToken)
                    .setNotification(notification)
                    .putAllData(notificationDetails.data)
                    .build()
            FirebaseMessaging.getInstance().send(message)
        }
    }

    @Throws(FirebaseMessagingException::class)
    fun sendBulkNotification(notificationDetails: NotificationDetailsModel) {
        notificationDetails.userIds?.map {
            val fcmToken =  getFCMToken(it)
            if(!fcmToken.isNullOrEmpty()) {
                val notification: Notification = Notification
                        .builder()
                        .setTitle(notificationDetails.title)
                        .setBody(notificationDetails.body)
                        .build()
                val message: Message = Message
                        .builder()
                        .setToken(fcmToken)
                        .setNotification(notification)
                        .putAllData(notificationDetails.data)
                        .build()
                FirebaseMessaging.getInstance().send(message)
            }
        }
    }

    fun getFCMToken(userId: String): String? {
        val commonDetails = commonDetailsRepository.findByFirebaseUserId(userId)
        if(commonDetails.isPresent) {
            return commonDetails.get().fcmToken
        }
        return null
    }
}