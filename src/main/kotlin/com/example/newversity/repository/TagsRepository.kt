package com.example.newversity.repository

import com.example.newversity.entity.Tags
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface TagsRepository: MongoRepository<Tags, String> {
    fun findByTagName(tagName: String): Optional<Tags>
}