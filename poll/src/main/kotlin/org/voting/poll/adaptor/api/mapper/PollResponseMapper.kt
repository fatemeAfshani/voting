package org.voting.poll.adaptor.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import org.voting.poll.adaptor.infrastructure.MappingConfiguration
import org.voting.poll.domain.poll.PollModel
import poll.Poll.PollResponse


@Mapper(config = MappingConfiguration::class)
interface PollResponseMapper {

    companion object {
        val mapper: PollResponseMapper = Mappers.getMapper(PollResponseMapper::class.java)
    }

    @Mapping(target = "pollId", source = "id")
    fun dtoToProto(dto: PollModel?): PollResponse


}
