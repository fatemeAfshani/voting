package org.voting.poll.adaptor.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import org.voting.poll.adaptor.infrastructure.MappingConfiguration
import org.voting.poll.domain.poll.dto.UpdatePollDTO
import poll.Poll


@Mapper(config = MappingConfiguration::class)
interface UpdatePollMapper: BaseMapper {

    companion object {
        val mapper: UpdatePollMapper = Mappers.getMapper(UpdatePollMapper::class.java)
    }

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "role", expression = "java(mapRole(role))")
    // todo: check do we need below mappers
    @Mapping(target = "maxVoters", source = "proto.maxVoters")
    @Mapping(target = "title", source = "proto.title")
    @Mapping(target = "description", source = "proto.description")
    fun protoToDto(proto: Poll.UpdatePollRequest, role: String, userId: String): UpdatePollDTO


}
