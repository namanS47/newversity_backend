package com.example.NewVersity.services.teacher

import com.example.NewVersity.entity.Tags
import com.example.NewVersity.model.TagModel
import com.example.NewVersity.repository.TagsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class TagsService(
        @Autowired val tagsRepository: TagsRepository
) {
    @Async
    fun updateTagList(tagList: List<TagModel>, teacherId: String) {
        val existingTagList = getAllTagsWithTeacherId(teacherId)
        existingTagList.forEach { existingTag->
            var removeTeacherIdFromTag = true
            tagList.forEach { newTag->
                if(newTag.tagName == existingTag.tagName){
                    removeTeacherIdFromTag = false
                }
            }
            if(removeTeacherIdFromTag) {
                existingTag.teacherIdList?.remove(teacherId)
                tagsRepository.save(existingTag)
            }
        }

        mapNewTags(tagList, teacherId)
    }

    fun getAllTagsWithTeacherId(teacherId: String): List<Tags> {
        val tagsList = tagsRepository.findAll()
        val filteredTagsList = tagsList.filter {
            it.teacherIdList?.contains(teacherId) ?: false
        }
        return filteredTagsList
    }

    fun mapNewTags(tagList: List<TagModel>, teacherId: String?) {
        tagList.forEach {
            val tag = it.tagName?.let { it1 -> tagsRepository.findByTagName(it1) }
            if (tag != null) {
                if(tag.isPresent) {
                    val list = tag.get().teacherIdList ?: ArrayList()
                    if (teacherId != null) {
                        list.add(teacherId)
                    }
                    tagsRepository.save(tag.get())
                } else {
                    val newTag = Tags()
                    val teacherList= mutableSetOf<String>()
                    if (teacherId != null) {
                        teacherList.add(teacherId)
                    }
                    newTag.apply {
                        tagName = it.tagName
                        teacherIdList = teacherList
                        tagCategory = it.tagCategory
                    }
                    tagsRepository.save(newTag)
                }
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