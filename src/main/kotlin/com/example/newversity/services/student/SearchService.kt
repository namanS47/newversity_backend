package com.example.newversity.services.student

import com.example.newversity.repository.TagsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class SearchService(
        @Autowired val tagsRepository: TagsRepository
) {
    fun searchTag(tagName: String) : ResponseEntity<*> {
        val allTagsList = tagsRepository.findAll().map {
            it.tagName
        }
        val resultedTags = allTagsList.filter {
            it?.contains(tagName) ?: false
        }
        return ResponseEntity.ok(resultedTags)
    }
}