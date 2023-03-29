package com.example.newversity.model

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
class TagListModel(
        var tagModelList: List<TagModel>? = null
)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
class TagModel (
        var tagName: String? = null,
        var tagCategory: String? = null
)

//object TagConvertor {
//    fun toEntity(tagModel: TagModel, teacherIds: Set<String>) : Tags {
//        val entity = Tags()
//        entity.apply {
//            tagName = tagModel.tagName
//            teacherIdList = teacherIds
//            tagCategory = tagModel.tagCategory
//        }
//        return entity
//    }
//
//    fun toModel(tags: Tags): TagModel {
//        val model = TagModel()
//        model.apply {
//            tagName = tags.tagName
//            tagCategory = tags.tagCategory
//        }
//        return model
//    }
//}