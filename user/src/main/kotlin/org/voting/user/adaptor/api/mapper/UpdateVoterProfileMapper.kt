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
    @Mapping(source = "proto.city", target = "city")
    @Mapping(source = "proto.age", target = "age")
    @Mapping(source = "proto.job", target = "job")
    @Mapping(source = "proto.fieldOfStudy", target = "fieldOfStudy")
    @Mapping(
        target = "gender",
        expression = "java(org.voting.user.adaptor.api.mapper.EnumValidators.mapGenderOptional(proto))"
    )
    @Mapping(
        target = "educationLevel",
        expression = "java(org.voting.user.adaptor.api.mapper.EnumValidators.mapEducationLevelOptional(proto))"
    )
    @Mapping(
        target = "maritalStatus",
        expression = "java(org.voting.user.adaptor.api.mapper.EnumValidators.mapMaritalStatusOptional(proto))"
    )
    fun protoToDto(proto: User.UpdateVoterProfileRequest, userId: String, userRole: String): UpdateProfileDto
}
