package org.voting.poll.adaptor.persistance.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import org.voting.poll.adaptor.persistance.entity.Poll
import org.voting.poll.domain.poll.PollModel
import org.voting.poll.adaptor.infrastructure.MappingConfiguration


@Mapper(config = MappingConfiguration::class)
interface PollMapper {

    companion object {
        val mapper: PollMapper = Mappers.getMapper(PollMapper::class.java)
    }


    @Mapping(source = "id", target = "id")
    fun modelToEntity(model: PollModel?): Poll?

    @Mapping(source = "id", target = "id")
    fun entityToModel(entity: Poll?): PollModel?
}
