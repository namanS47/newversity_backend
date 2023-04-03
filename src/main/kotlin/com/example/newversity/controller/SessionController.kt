package com.example.newversity.controller

import com.example.newversity.model.SessionModel
import com.example.newversity.services.teacher.SessionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/session")
class SessionController(
        @Autowired val sessionService: SessionService
) {
    @PostMapping("/add")
    fun saveSession(@RequestBody sessionModel: SessionModel) : ResponseEntity<*> {
        return sessionService.addSession(sessionModel)
    }

    @GetMapping("/teacher")
    fun getSessionByTeacherId(@RequestHeader teacherId: String, @RequestParam("type") type: String = ""): ResponseEntity<*> {
        return sessionService.getSessionByTeacherId(teacherId, type)
    }
}