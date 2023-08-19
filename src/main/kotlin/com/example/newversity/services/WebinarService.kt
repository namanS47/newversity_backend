package com.example.newversity.services

import com.example.newversity.model.WebinarConvertor
import com.example.newversity.model.WebinarModel
import com.example.newversity.repository.WebinarRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class WebinarService(
        @Autowired val webinarRepository: WebinarRepository
) {
    fun addWebinar(webinarModel: WebinarModel): ResponseEntity<*> {
        val webinar = webinarRepository.save(WebinarConvertor.toEntity(webinarModel))
        return ResponseEntity.ok().body(webinar)
    }
}