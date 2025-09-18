package org.voting.poll.adaptor.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ValueMapping
import org.mapstruct.ValueMappings
import org.mapstruct.factory.Mappers
import org.voting.poll.adaptor.infrastructure.MappingConfiguration
import org.voting.poll.domain.poll.PollOption
import org.voting.poll.domain.poll.PollQuestion
import org.voting.poll.domain.vote.dto.StartVoteDTO
import org.voting.poll.domain.vote.dto.StartVoteResponse
import poll.Poll
import poll.Poll.Option
import poll.Poll.Question
import poll.Poll.StartVotingResponse
import org.voting.poll.domain.poll.enums.QuestionType as DomainQuestionType
import poll.Poll.QuestionType as ProtoQuestionType

@Mapper(config = MappingConfiguration::class)
interface StartVotingMapper : BaseMapper {
    companion object {
        val mapper: StartVotingMapper = Mappers.getMapper(StartVotingMapper::class.java)
    }


    fun mapOption(dto: List<PollOption>): List<Option>

    @ValueMappings(
        ValueMapping(source = "EXPLAIN", target = "EXPLAIN"),
        ValueMapping(source = "TWO_OPTION", target = "TWO_OPTION"),
        ValueMapping(source = "MULTIPLE_CHOICE", target = "MULTIPLE_CHOICE")
    )
    fun mapQuestionType(type: DomainQuestionType): ProtoQuestionType

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "role", expression = "java(mapRole(role))")
    @Mapping(target = "pollId", expression = "java(proto.getPollId())")
    fun protoToDto(proto: Poll.StartVotingRequest, role: String, userId: String): StartVoteDTO
}
