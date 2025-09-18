package org.voting.user.domain.voter

import org.voting.user.adaptor.exception.Errors
import org.voting.user.domain.user.EducationLevels
import org.voting.user.domain.user.Genders
import org.voting.user.domain.user.MaritalStatuses
import org.voting.user.domain.voter.dto.UpdateProfileDto
import java.util.*

data class VoterModel(
    val id: String? = null,
    val telegramId: String,
    val userId: String? = null,
    var city: String? = null,
    var gender: Genders? = null,
    var age: Int? = null,
    var job: String? = null,
    var educationLevel: EducationLevels? = null,
    var fieldOfStudy: String? = null,
    var maritalStatus: MaritalStatuses? = null,
    var createDate: Date? = null,
    var updatedAt: Date? = null,
    var version: Long? = null,
) {

    fun updateUserProfile(data: UpdateProfileDto) {
        this.city = data.city
        this.gender = data.gender
        this.age = data.age
        this.job = data.job
        this.educationLevel = data.educationLevel
        this.fieldOfStudy = data.fieldOfStudy
        this.maritalStatus = data.maritalStatus
    }
    fun validate() {
        age?.let {
            require(it in 0..100) { Errors.ErrorCodes.AGE_IS_INVALID.name }
        }

        gender?.let {
            require(it in Genders.entries.toTypedArray()) { Errors.ErrorCodes.GENDER_IS_INVALID.name }
        }

        job?.let {
            require(it.length <= 100) { Errors.ErrorCodes.JOB_IS_INVALID.name }
        }

        educationLevel?.let {
            require(
                it in EducationLevels.entries.toTypedArray()
            ) { Errors.ErrorCodes.EDUCATION_LEVEL_IS_INVALID.name }
        }

        fieldOfStudy?.let {
            require(it.length <= 100) { Errors.ErrorCodes.FIELD_OF_STUDY_IS_INVALID.name }
        }

        maritalStatus?.let {
            require(
                it in MaritalStatuses.entries.toTypedArray()
            ) { Errors.ErrorCodes.MARITAL_STATUS_IS_INVALID.name }
        }

        city?.let {
            require(it.length <= 100) { Errors.ErrorCodes.CITY_IS_INVALID.name }
        }
    }
}
