package org.voting.poll.adaptor.persistance.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import org.voting.poll.adaptor.persistance.entity.Poll
import org.voting.poll.domain.poll.PollModel
import org.voting.poll.adaptor.infrastructure.MappingConfiguration


@Mapper(config = MappingConfiguration::class)
interface PollMapper {

    companion object {
        val mapper: PollMapper = Mappers.getMapper(PollMapper::class.java)
    }

    fun modelToEntity(model: PollModel?): Poll?

    fun entityToModel(entity: Poll?): PollModel?
}
