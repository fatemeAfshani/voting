package org.voting.poll.adaptor.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import org.voting.poll.adaptor.exception.Errors
import org.voting.poll.adaptor.exception.ForbiddenException
import org.voting.poll.adaptor.exception.InvalidInputException
import org.voting.poll.adaptor.infrastructure.MappingConfiguration
import org.voting.poll.domain.poll.dto.CreatePollDTO
import org.voting.poll.domain.poll.enums.Roles
import poll.Poll


@Mapper(config = MappingConfiguration::class)
interface CreatePollMapper: BaseMapper {

    companion object {
        val mapper: CreatePollMapper = Mappers.getMapper(CreatePollMapper::class.java)
    }

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "role", expression = "java(mapRole(role))")
    fun protoToDto(proto: Poll.CreatePollRequest, role: String, userId: String): CreatePollDTO


}
