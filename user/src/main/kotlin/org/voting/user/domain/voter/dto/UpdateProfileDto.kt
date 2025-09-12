package org.voting.user.domain.voter.dto

import org.voting.user.domain.user.EducationLevels
import org.voting.user.domain.user.Genders
import org.voting.user.domain.user.MaritalStatuses
import org.voting.user.domain.user.Roles

data class UpdateProfileDto(
    val city: String? = null,
    val gender: Genders? = null,
    val age: Int? = null,
    val job: String? = null,
    val educationLevel: EducationLevels? = null,
    val fieldOfStudy: String? = null,
    val maritalStatus: MaritalStatuses? = null,
    val userId: String?,
    val userRole: Roles?
)
