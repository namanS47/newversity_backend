package com.example.newversity.services

import com.example.newversity.repository.RedisRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class RedisService(
        private val redisTemplate: RedisTemplate<String, Any>
) : RedisRepository {

    override fun save(key: String, value: ArrayList<String>) {
        redisTemplate.opsForValue().set(key, value)
    }

    override fun find(key: String): ArrayList<String>? {
        val result = redisTemplate.opsForValue().get(key)
        return if (result != null) result as ArrayList<String> else null
    }
}