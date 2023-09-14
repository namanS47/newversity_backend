package com.example.newversity.repository

import com.example.newversity.entity.NotificationDetailsEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface NotificationRepository: MongoRepository<NotificationDetailsEntity, String> {
    fun findByUserId(userId: String): List<NotificationDetailsEntity>
}