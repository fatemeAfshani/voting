package org.voting.user.adaptor.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import org.voting.user.adaptor.infrastructure.MappingConfiguration
import org.voting.user.domain.voter.dto.UpdateProfileDto
import user.User

@Mapper(config = MappingConfiguration::class)
interface UpdateVoterProfileMapper : BaseMapper {

    companion object {
        val mapper: UpdateVoterProfileMapper = Mappers.getMapper(UpdateVoterProfileMapper::class.java)
    }

    @Mapping(source = "userId", target = "userId")
    @Mapping(target = "userRole", expression = "java(mapRole(userRole))")
    @Mapping(
        target = "gender",
        expression = "java(org.voting.user.adaptor.api.mapper.EnumValidators.mapGenderStrict(proto.getGender()))"
    )
    @Mapping(
        target = "educationLevel",
        expression =
        "java(org.voting.user.adaptor.api.mapper.EnumValidators.mapEducationLevelStrict(proto.getEducationLevel()))"
    )
    @Mapping(
        target = "maritalStatus",
        expression =
        "java(org.voting.user.adaptor.api.mapper.EnumValidators.mapMaritalStatusStrict(proto.getMaritalStatus()))"
    )
    fun protoToDto(proto: User.UpdateVoterProfileRequest, userId: String, userRole: String): UpdateProfileDto
}
