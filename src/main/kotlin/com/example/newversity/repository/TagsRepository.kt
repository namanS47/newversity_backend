package com.example.newversity.repository

import com.example.newversity.entity.teacher.Tags
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface TagsRepository: MongoRepository<Tags, String> {
    fun findByTagNameIgnoreCase(tagName: String): Optional<Tags>
}