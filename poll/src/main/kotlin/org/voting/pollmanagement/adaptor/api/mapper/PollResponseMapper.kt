package org.voting.pollmanagement.adaptor.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import org.voting.pollmanagement.adaptor.exception.Error
import org.voting.pollmanagement.adaptor.exception.ForbiddenException
import org.voting.pollmanagement.adaptor.exception.InvalidInputException
import org.voting.pollmanagement.adaptor.infrastructure.MappingConfiguration
import org.voting.pollmanagement.domain.poll.PollModel
import org.voting.pollmanagement.domain.poll.dto.CreatePollDTO
import org.voting.pollmanagement.domain.poll.enums.Roles
import pollManagement.Poll
import pollManagement.Poll.PollResponse


@Mapper(config = MappingConfiguration::class)
interface PollResponseMapper {

    companion object {
        val mapper: PollResponseMapper = Mappers.getMapper(PollResponseMapper::class.java)
    }

    @Mapping(target = "pollId", source = "id")
    fun dtoToProto(dto: PollModel?): PollResponse


}
