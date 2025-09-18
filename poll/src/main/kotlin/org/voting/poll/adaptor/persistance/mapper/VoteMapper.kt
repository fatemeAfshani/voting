package org.voting.poll.adaptor.persistance.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import org.voting.poll.adaptor.infrastructure.MappingConfiguration
import org.voting.poll.adaptor.persistance.entity.Vote
import org.voting.poll.domain.vote.VoteModel

@Mapper(config = MappingConfiguration::class)
interface VoteMapper {
    companion object {
        val mapper: VoteMapper = Mappers.getMapper(VoteMapper::class.java)
    }

    fun entityToModel(entity: Vote): VoteModel

    @Mapping(target = "id", source = "id")
    @Mapping(target = "pollId", source = "pollId")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "answers", source = "answers")
    fun modelToEntity(model: VoteModel): Vote
}


