package com.example.NewVersity.entity

import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "tags")
class Tags (
        @Indexed
        @Field("tag_name")
        var tagName: String? = null,

        @Field("teacher_id_list")
        var teacherIdList: MutableSet<String>?    = null,

        @Field("tag_category")
        var tagCategory: String? = null
): AppEntity()