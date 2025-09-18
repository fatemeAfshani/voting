package org.voting.poll.adaptor.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import org.voting.poll.adaptor.infrastructure.MappingConfiguration
import org.voting.poll.domain.vote.dto.StartVoteDTO
import poll.Poll

@Mapper(config = MappingConfiguration::class)
interface StartVotingMapper : BaseMapper {
    companion object {
        val mapper: StartVotingMapper = Mappers.getMapper(StartVotingMapper::class.java)
    }

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "role", expression = "java(mapRole(role))")
    @Mapping(target = "pollId", expression = "java(proto.getPollId())")
    fun protoToDto(proto: Poll.StartVotingRequest, role: String, userId: String): StartVoteDTO
}
