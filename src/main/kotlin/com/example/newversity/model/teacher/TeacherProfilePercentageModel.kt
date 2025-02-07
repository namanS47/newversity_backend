package com.example.newversity.model.teacher

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class TeacherProfilePercentageModel(
        var completePercentage: Int? = null,
        var reason: String? = null,
        var suggestion: String? = null,
        var profileCompletionStageStatus: HashMap<ProfileCompletionStage, Boolean>? = null,
)

enum class ProfileCompletionStage {
    VerifiedTags, SelectTags, Pricing, Experience, Education, Profile, ProfilePicture
}