package com.example.newversity.model

import com.example.newversity.entity.common.WebinarDetails
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class WebinarModel (
        var id: String? = null,
        var teacherId: String? = null,
        var studentId: String? = null,
        var title: String? = null,
        var feedback: String? = null,
        var rating: Double? = null,
        var agenda: String? = null,
        var issueRaised: String? = null,
)

object WebinarConvertor {
    fun toEntity(webinarModel: WebinarModel) : WebinarDetails {
        val entity = WebinarDetails()
        entity.apply {
            teacherId= webinarModel.teacherId
            studentId = webinarModel.studentId
            title= webinarModel.title
            feedback = webinarModel.feedback
            rating = webinarModel.rating
            agenda = webinarModel.agenda
            issueRaised = webinarModel.issueRaised
        }
        return entity
    }

    fun toModel(webinarDetails: WebinarDetails) : WebinarModel {
        val model = WebinarModel()
        model.apply {
            teacherId= webinarDetails.teacherId
            studentId = webinarDetails.studentId
            title= webinarDetails.title
            feedback = webinarDetails.feedback
            rating = webinarDetails.rating
            issueRaised = webinarDetails.issueRaised
            agenda = webinarDetails.agenda
        }
        return model
    }
}