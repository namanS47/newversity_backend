package com.example.newversity.model

import com.example.newversity.entity.TeacherDetails

data class TeacherAndTagDetailModel (
        var teacherDetail: TeacherDetailModel? = null,
        var teacherTagList: List<TagModel>? = null,
)

object TeacherAndTagDetailConvertor {
    fun toModel(teacherDetails: TeacherDetails, tagList: List<TagModel>?) : TeacherAndTagDetailModel {
        val teacherAndTagDetailModel = TeacherAndTagDetailModel()
        teacherAndTagDetailModel.apply {
            teacherDetail = TeacherConverter.toModel(teacherDetails)
            teacherTagList = tagList
        }
        return teacherAndTagDetailModel
    }
}
