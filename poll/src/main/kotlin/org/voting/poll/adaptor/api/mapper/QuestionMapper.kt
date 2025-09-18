package org.voting.poll.adaptor.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.ValueMapping
import org.mapstruct.ValueMappings
import org.mapstruct.factory.Mappers
import org.voting.poll.adaptor.infrastructure.MappingConfiguration
import org.voting.poll.domain.poll.PollOption
import org.voting.poll.domain.poll.PollQuestion
import org.voting.poll.domain.poll.enums.QuestionType
import poll.Poll

@Mapper(config = MappingConfiguration::class)
interface QuestionMapper {

    companion object {
        val mapper: QuestionMapper = Mappers.getMapper(QuestionMapper::class.java)
    }
    fun mapOption(dto: List<PollOption>): List<Poll.Option>

    @ValueMappings(
        ValueMapping(source = "EXPLAIN", target = "EXPLAIN"),
        ValueMapping(source = "TWO_OPTION", target = "TWO_OPTION"),
        ValueMapping(source = "MULTIPLE_CHOICE", target = "MULTIPLE_CHOICE")
    )
    fun mapQuestionType(type: QuestionType): Poll.QuestionType
}

fun createQuestionProto(question: PollQuestion, pollId: String?): Poll.Question {
    return Poll.Question.newBuilder()
        .setQuestionId(question.questionId)
        .setPollId(pollId)
        .setQuestionType(QuestionMapper.mapper.mapQuestionType(question.questionType))
        .setQuestionText(question.questionText)
        .setShouldAnswer(question.shouldAnswer)
        .addAllOptions(QuestionMapper.mapper.mapOption(question.options))
        .build()
}
