package com.example.newversity.services.student

import com.example.newversity.services.teacher.TagsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class SearchService(
        @Autowired val tagsService: TagsService
) {
    fun searchTags(tagName: String): ResponseEntity<*> {
        val allTagsList = tagsService.getAllTags(true).filter {
            it.tagName?.lowercase(Locale.getDefault())?.contains(tagName.lowercase(Locale.getDefault())) ?: false
        }
        return ResponseEntity.ok(allTagsList)
    }
}