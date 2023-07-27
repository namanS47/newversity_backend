package com.example.newversity.controller

import com.amazonaws.Response
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

    @GetMapping("/id")
    fun getSessionById(@RequestHeader id: String) : ResponseEntity<*> {
        return sessionService.getSessionById(id)
    }

    @GetMapping("/student")
    fun getSessionByStudentId(@RequestHeader studentId: String, @RequestParam("type") type: String = ""): ResponseEntity<*> {
        return sessionService.getSessionByStudentId(studentId, type)
    }

    @GetMapping("/count")
    fun getTotalSessionCount(@RequestHeader teacherId: String) : ResponseEntity<*> {
        return sessionService.getAllSessionCountByTeacherId(teacherId)
    }
}