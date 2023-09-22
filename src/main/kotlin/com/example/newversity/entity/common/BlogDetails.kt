package com.example.newversity.entity.common

import com.example.newversity.entity.teacher.AppEntity
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "blogs")
class BlogDetails (
        var title: String? = null,

        var content: String? = null,

        @Field("feature_image")
        var featureImage: String? = null,
) : AppEntity()