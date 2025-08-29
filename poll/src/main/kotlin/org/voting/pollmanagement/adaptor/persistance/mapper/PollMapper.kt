package org.voting.pollmanagement.adaptor.persistance.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import org.voting.pollmanagement.adaptor.persistance.entity.Poll
import org.voting.pollmanagement.domain.poll.PollModel
import org.voting.pollmanagement.adaptor.infrastructure.MappingConfiguration


@Mapper(config = MappingConfiguration::class)
interface PollMapper {

    companion object {
        val mapper: PollMapper = Mappers.getMapper(PollMapper::class.java)
    }

    fun modelToEntity(model: PollModel?): Poll?

    fun entityToModel(entity: Poll?): PollModel?
}