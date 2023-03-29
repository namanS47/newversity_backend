package com.example.newversity.services.room

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
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

    private val roomAccessKey =  "6409d44bedc7c8f3674c0d3b"
    private val roomAppSecret = "LqOM_XoSqd2ta6_3fTz8Mzh6AgNRXIJEdeTqMN7_kaxAr5SqLfAVkoE9eq7useehLZmrbyyTgKnVYIbW1oFoJniBm0EhCOXJ89fuQgeGbVVzENWOulZoi0jdwBmDKLZD6WFs3cAbVoDLvvrs-64y2h-TvWydhk2aSktREMcpOoY="

    fun generateHmsClientToken(): String {
        val payload: MutableMap<String, Any?> = HashMap()
        payload["access_key"] = roomAccessKey
        payload["room_id"] = "640a5af9db104c4c44befdf8"
        payload["user_id"] = "namanUserId"
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

    fun generateRoom(): String {
        val uri = "https://api.100ms.live/v2/rooms"

        val restTemplate = RestTemplate()
//        val headers = HttpHeaders()
//        headers.contentType = MediaType.APPLICATION_JSON.toString()
//        headers.authorization = "Bearer ${generateManagementToken()}"

        val headers = LinkedMultiValueMap<String, String>()
        headers["Content-Type"] = MediaType.APPLICATION_JSON.toString()
        headers["Authorization"] = "Bearer ${generateManagementToken()}"

        val map: HashMap<String, Any> = HashMap()
        map["name"] = "new-room-1662723668"
        map["description"] = "This is a sample description for the room"
        map["template_id"] = "6409d7b45ec8ce8ab3c02b75"
        map["region"] = "in"

        val entity: HttpEntity<Map<String, Any>> = HttpEntity(map, headers)
        val response: ResponseEntity<String> = restTemplate.postForEntity<String>(uri, entity, String::class.java)

        println("naman1---${response}")
        return "hello"
    }
}