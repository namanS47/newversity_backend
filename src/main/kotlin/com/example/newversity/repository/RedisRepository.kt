package com.example.newversity.repository

import org.springframework.stereotype.Repository

@Repository
interface RedisRepository {

    fun save(key: String, value: ArrayList<String>)

    fun find(key: String): ArrayList<String>?
}