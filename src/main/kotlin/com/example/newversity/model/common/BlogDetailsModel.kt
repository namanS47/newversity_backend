package com.example.newversity.model.common

import com.example.newversity.entity.common.BlogDetails
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.Date

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class BlogDetailsModel (
        var id: String? = null,
        var title: String? = null,
        var content: String? = null,
        var featureImage: String? = null,
        var createdDate: Date? = null,
)

object BlogDetailsConvertor{
    fun toEntity(model: BlogDetailsModel): BlogDetails {
        val entity = BlogDetails()
        entity.apply {
            title = model.title
            content = model.content
            featureImage = model.featureImage
        }
        return entity
    }

    fun toModel(entity: BlogDetails): BlogDetailsModel {
        val model = BlogDetailsModel()
        model.apply {
            id = entity.id
            title = entity.title
            content = entity.content
            featureImage = entity.featureImage
            createdDate = entity.createdAt
        }
        return model
    }
}