package com.example.newversity.services.student

import com.example.newversity.repository.TagsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class SearchService(
        @Autowired val tagsRepository: TagsRepository
) {
    fun searchTag(tagName: String) : ResponseEntity<*> {
        val allTagsList = tagsRepository.findAll().map {
            it.tagName
        }
        val resultedTags = allTagsList.filter {
            it?.lowercase(Locale.getDefault())?.contains(tagName.lowercase(Locale.getDefault())) ?: false
        }
        return ResponseEntity.ok(resultedTags)
    }
}