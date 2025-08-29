package org.voting.pollmanagement.adaptor.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import org.voting.pollmanagement.adaptor.exception.Error
import org.voting.pollmanagement.adaptor.exception.ForbiddenException
import org.voting.pollmanagement.adaptor.exception.InvalidInputException
import org.voting.pollmanagement.adaptor.infrastructure.MappingConfiguration
import org.voting.pollmanagement.domain.poll.dto.CreatePollDTO
import org.voting.pollmanagement.domain.poll.enums.Roles
import pollManagement.Poll


@Mapper(config = MappingConfiguration::class)
interface CreatePollMapper {

    companion object {
        val mapper: CreatePollMapper = Mappers.getMapper(CreatePollMapper::class.java)
    }

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "role", expression = "java(mapRole(role))")
    fun protoToDto(proto: Poll.CreatePollRequest, role: String, userId: String): CreatePollDTO

    fun mapRole(role: String): Roles {
         try {
           val convertedRole =  Roles.valueOf(role.uppercase())
            if(convertedRole !== Roles.CREATOR){
                throw ForbiddenException(Error.ErrorCodes.FORBIDDEN.name)
            }
             return convertedRole
        } catch (e: IllegalArgumentException) {
            throw InvalidInputException(Error.ErrorCodes.INVALID_ROLE.name)
        }
    }
}
