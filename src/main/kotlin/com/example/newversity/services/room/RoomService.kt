package com.example.newversity.services.room

import com.example.newversity.entity.Session
import com.example.newversity.model.room.GenerateRoomResponseModel
import com.google.gson.Gson
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.bson.json.JsonObject
import org.springframework.http.HttpEntity
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import java.time.Instant
import java.util.*

@Service
class RoomService {

    private val roomAccessKey = "6409d44bedc7c8f3674c0d3b"
    private val roomAppSecret = "LqOM_XoSqd2ta6_3fTz8Mzh6AgNRXIJEdeTqMN7_kaxAr5SqLfAVkoE9eq7useehLZmrbyyTgKnVYIbW1oFoJniBm0EhCOXJ89fuQgeGbVVzENWOulZoi0jdwBmDKLZD6WFs3cAbVoDLvvrs-64y2h-TvWydhk2aSktREMcpOoY="

    fun sessionAuthTokenForRoom(session: Session): Session {
        session.roomResponseModel = session.id?.let { generateRoom(it) }
        session.studentToken = session.studentId?.let { session.roomResponseModel?.id?.let { it1 -> generateHmsClientToken(it, it1) } }
        session.teacherToken = session.teacherId?.let { session.roomResponseModel?.id?.let { it1 -> generateHmsClientToken(it, it1) } }
        return session
    }

    fun generateHmsClientToken(userId: String, roomId: String): String {
        val payload: MutableMap<String, Any?> = HashMap()
        payload["access_key"] = roomAccessKey
        payload["room_id"] = roomId
        payload["user_id"] = userId
        payload["role"] = "guest"
        payload["type"] = "app"
        payload["version"] = 2
        return Jwts.builder().setClaims(payload).setId(UUID.randomUUID().toString())
                .setExpiration(Date(System.currentTimeMillis() + 86400 * 1000))
                .setIssuedAt(Date.from(Instant.ofEpochMilli(System.currentTimeMillis() - 60000)))
                .setNotBefore(Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS256, roomAppSecret.toByteArray()).compact()
    }


    private fun generateManagementToken(): String {
        val payload: MutableMap<String, Any?> = HashMap()
        payload["access_key"] = roomAccessKey
        payload["type"] = "management"
        payload["version"] = 2
        return Jwts.builder().setClaims(payload).setId(UUID.randomUUID().toString())
                .setExpiration(Date(System.currentTimeMillis() + 86400 * 1000))
                .setIssuedAt(Date.from(Instant.ofEpochMilli(System.currentTimeMillis() - 60000)))
                .setNotBefore(Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS256, roomAppSecret.toByteArray()).compact()
    }

    fun generateRoom(roomName: String): GenerateRoomResponseModel {
        val uri = "https://api.100ms.live/v2/rooms"

        val restTemplate = RestTemplate()
        val headers = LinkedMultiValueMap<String, String>()
        headers["Content-Type"] = MediaType.APPLICATION_JSON.toString()
        headers["Authorization"] = "Bearer ${generateManagementToken()}"

        val map: HashMap<String, Any> = HashMap()
        map["name"] = roomName
        map["template_id"] = "6409d7b45ec8ce8ab3c02b75"
        map["region"] = "in"

        val entity: HttpEntity<Map<String, Any>> = HttpEntity(map, headers)
        val response = restTemplate.postForEntity(uri, entity, String::class.java)
        return Gson().fromJson(response.body, GenerateRoomResponseModel::class.java)
    }
}