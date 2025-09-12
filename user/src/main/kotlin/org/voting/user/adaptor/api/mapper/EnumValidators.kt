package org.voting.user.adaptor.api.mapper

import org.voting.user.adaptor.exception.Errors
import org.voting.user.adaptor.exception.InvalidException
import org.voting.user.domain.user.EducationLevels
import org.voting.user.domain.user.Genders
import org.voting.user.domain.user.MaritalStatuses
import user.User

object EnumValidators {
    @JvmStatic
    fun mapGenderStrict(type: User.Genders): Genders {
        return when (type) {
            User.Genders.MALE -> Genders.MALE
            User.Genders.FEMALE -> Genders.FEMALE
            User.Genders.UNRECOGNIZED -> throw InvalidException(Errors.ErrorCodes.GENDER_IS_INVALID.name)
        }
    }

    @JvmStatic
    fun mapGenderOptional(proto: User.UpdateVoterProfileRequest): Genders? {
        return if (proto.hasGender()) {
            mapGenderStrict(proto.gender)
        } else {
            null
        }
    }

    @JvmStatic
    fun mapEducationLevelStrict(type: User.EducationLevels): EducationLevels {
        return when (type) {
            User.EducationLevels.STUDENT -> EducationLevels.STUDENT
            User.EducationLevels.BACHELOR -> EducationLevels.BACHELOR
            User.EducationLevels.MASTER -> EducationLevels.MASTER
            User.EducationLevels.DOCTORATE -> EducationLevels.DOCTORATE
            User.EducationLevels.OTHER -> EducationLevels.OTHER
            User.EducationLevels.UNRECOGNIZED ->
                throw InvalidException(Errors.ErrorCodes.EDUCATION_LEVEL_IS_INVALID.name)
        }
    }

    @JvmStatic
    fun mapEducationLevelOptional(proto: User.UpdateVoterProfileRequest): EducationLevels? {
        return if (proto.hasEducationLevel()) {
            mapEducationLevelStrict(proto.educationLevel)
        } else {
            null
        }
    }

    @JvmStatic
    fun mapMaritalStatusStrict(type: User.MaritalStatuses): MaritalStatuses {
        return when (type) {
            User.MaritalStatuses.MARRIED -> MaritalStatuses.MARRIED
            User.MaritalStatuses.SINGLE -> MaritalStatuses.SINGLE
            User.MaritalStatuses.UNRECOGNIZED ->
                throw InvalidException(Errors.ErrorCodes.MARITAL_STATUS_IS_INVALID.name)
        }
    }

    @JvmStatic
    fun mapMaritalStatusOptional(proto: User.UpdateVoterProfileRequest): MaritalStatuses? {
        return if (proto.hasMaritalStatus()) {
            mapMaritalStatusStrict(proto.maritalStatus)
        } else {
            null
        }
    }
}


