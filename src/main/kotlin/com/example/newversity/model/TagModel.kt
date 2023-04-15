package com.example.newversity.model

import com.example.newversity.entity.Tags
import com.example.newversity.entity.TeacherTagDetails
import com.example.newversity.services.teacher.TagStatus
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class TagListModel(
        var tagModelList: List<TagModel>? = null
)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class TagModel (
        var tagName: String? = null,
        var tagCategory: String? = null,
        var teacherTagDetailList: MutableMap<String, TeacherTagDetailsModel>? = null,
        var teacherTagDetails: TeacherTagDetailsModel? = null,
)

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class TeacherTagDetailsModel(
        var tagStatus: TagStatus? = null,

        var documents: ArrayList<String>? = null,

        var reason: String? = null,

        var suggestion: String? = null,
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
            teacherTagDetailList = teacherTagDetailListModel(tags.teacherTagDetailList)
        }
        return model
    }

    fun toTeacherTagDetailModel(teacherTagDetail: TeacherTagDetails): TeacherTagDetailsModel {
        val model = TeacherTagDetailsModel()
        model.apply {
            tagStatus = teacherTagDetail.tagStatus
            documents = teacherTagDetail.documents
            reason = teacherTagDetail.reason
            suggestion = teacherTagDetail.suggestion
        }
        return model
    }

    private fun teacherTagDetailListModel(entity: MutableMap<String, TeacherTagDetails>?): MutableMap<String, TeacherTagDetailsModel>? {
        if(entity == null){
            return null
        }
        val model = mutableMapOf<String, TeacherTagDetailsModel>()
        entity.forEach{
            model[it.key] = toTeacherTagDetailModel(it.value)
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