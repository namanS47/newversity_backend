package com.example.newversity.model.common

import com.example.newversity.entity.common.RequestSessionDetails
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class RequestSessionModel(
        var id: String? = null,
        var teacherId: String? = null,
        var studentId: String? = null,
        var engagementType: List<EngagementType>? = null,
        var info: String? = null,
        var agenda: String? = null,
        var location: String? = null,
        var forCreators: Boolean? = null,
)

object RequestSessionModelConvertor {
    fun toEntity(requestSessionModel: RequestSessionModel): RequestSessionDetails {
        val entity = RequestSessionDetails()
        entity.apply {
            teacherId = requestSessionModel.teacherId
            studentId = requestSessionModel.studentId
            engagementType = requestSessionModel.engagementType
            info = requestSessionModel.info
            agenda = requestSessionModel.agenda
            location = requestSessionModel.location
            forCreators = requestSessionModel.forCreators
        }
        return entity
    }

    fun toModel(requestSessionDetails: RequestSessionDetails): RequestSessionModel {
        val model = RequestSessionModel()
        model.apply {
            id = requestSessionDetails.id
            teacherId = requestSessionDetails.teacherId
            studentId = requestSessionDetails.studentId
            engagementType = requestSessionDetails.engagementType
            info = requestSessionDetails.info
            agenda = requestSessionDetails.agenda
            location = requestSessionDetails.location
            forCreators = requestSessionDetails.forCreators
        }
        return model
    }
}

enum class EngagementType {
    OneOnOne, Webinar, AskMeAnything, Content, Other
}