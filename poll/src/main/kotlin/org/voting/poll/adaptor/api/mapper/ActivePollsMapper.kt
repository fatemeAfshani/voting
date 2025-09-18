package org.voting.poll.adaptor.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import org.voting.poll.adaptor.infrastructure.MappingConfiguration
import org.voting.poll.domain.poll.dto.ActivePollsDTO
import poll.Poll.PollInfo

@Mapper(config = MappingConfiguration::class)
interface ActivePollsMapper {

    companion object {
        val MAPPER: ActivePollsMapper = Mappers.getMapper(ActivePollsMapper::class.java)
    }

    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "preferences", source = "preferences")
    fun toProto(dto: ActivePollsDTO): PollInfo

    fun toProtoList(dtos: List<ActivePollsDTO>): List<PollInfo>
}
