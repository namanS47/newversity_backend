package com.example.newversity.model

import com.example.newversity.entity.Tags
import com.example.newversity.entity.TeacherTagDetails
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
class TagListModel(
        var tagModelList: List<TagModel>? = null
)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
class TagModel (
        var tagName: String? = null,
        var tagCategory: String? = null,
        var teacherTagDetailList: MutableMap<String, TeacherTagDetails>? = null,
        var teacherTagDetails: TeacherTagDetails? = null,
)



object TagConvertor {
//    fun toEntity(tagModel: TagModel, teacherIds: Set<String>) : Tags {
//        val entity = Tags()
//        entity.apply {
//            tagName = tagModel.tagName
//            teacherTagDetailList = teacherIds
//            tagCategory = tagModel.tagCategory
//        }
//        return entity
//    }

    fun toAllTagModel(tags: Tags): TagModel {
        val model = TagModel()
        model.apply {
            tagName = tags.tagName
            tagCategory = tags.tagCategory
            teacherTagDetailList = tags.teacherTagDetailList
        }
        return model
    }

    fun toTeacherTagModel(tags: Tags): TagModel {
        val model = TagModel()
        model.apply {
            tagName = tags.tagName
            tagCategory = tags.tagCategory
//            teacherTagDetails = tags.teacherTagDetailList
        }
        return model
    }
}