package com.example.NewVersity.services

import com.example.NewVersity.entity.Tags
import com.example.NewVersity.model.TagsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class TagsService(
        @Autowired val tagsRepository: TagsRepository
) {
    @Async
    fun updateTagList(updatedTags: List<String>, existingTags: List<String>, teacherId: String) {
        val newTagsToMap = updatedTags.filter {
            !existingTags.contains(it)
        }

        val tagsToRemove = existingTags.filter {
            !updatedTags.contains(it)
        }

        mapNewTagsWithTeacher(newTagsToMap, teacherId)
        removeOldTagsMappedWithTeacher(tagsToRemove, teacherId)
    }

    private fun mapNewTagsWithTeacher(tagList: List<String>, teacherId: String) {
        tagList.forEach {name->
            val tag = tagsRepository.findByTagName(name)
            if(tag.isPresent) {
                val list = tag.get().teacherIdList ?: ArrayList()
                if(!list.contains(teacherId)){
                    list.add(teacherId)
                    tagsRepository.save(tag.get())
                }
            } else {
                val newTag = Tags()
                val teacherList = ArrayList<String>()
                teacherList.add(teacherId)
                newTag.apply {
                    tagName = name
                    teacherIdList = teacherList
                }
                tagsRepository.save(newTag)
            }
        }
    }

    private fun removeOldTagsMappedWithTeacher(tagList: List<String>, teacherId: String) {
        tagList.forEach {tagName->
            val tag = tagsRepository.findByTagName(tagName)
            if(tag.isPresent) {
                val list = tag.get().teacherIdList ?: ArrayList()
                if(list.contains(teacherId)){
                    list.remove(teacherId)
                    tagsRepository.save(tag.get())
                }
            }
        }
    }

    @Async
    fun testing() {
        for(a in 1.. 1000000) {
            print(a)
        }
        println("naman3")
    }
}