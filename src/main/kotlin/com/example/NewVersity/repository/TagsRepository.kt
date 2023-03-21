package com.example.NewVersity.repository

import com.example.NewVersity.entity.Tags
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface TagsRepository: MongoRepository<Tags, String> {
    fun findByTagName(tagName: String): Optional<Tags>
}