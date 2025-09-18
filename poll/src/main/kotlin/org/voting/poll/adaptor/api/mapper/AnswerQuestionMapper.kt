package org.voting.poll.adaptor.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import org.voting.poll.adaptor.infrastructure.MappingConfiguration
import org.voting.poll.domain.vote.dto.AnswerQuestionDTO
import poll.Poll.AnswerQuestionRequest

@Mapper(config = MappingConfiguration::class, uses = [StartVotingMapper::class])
interface AnswerQuestionMapper : BaseMapper {
    companion object {
        val mapper: AnswerQuestionMapper = Mappers.getMapper(AnswerQuestionMapper::class.java)
    }

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "role", expression = "java(mapRole(role))")
    fun protoToDto(proto: AnswerQuestionRequest, role: String, userId: String): AnswerQuestionDTO
}
