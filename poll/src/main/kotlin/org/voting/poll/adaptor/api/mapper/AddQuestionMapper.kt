package org.voting.poll.adaptor.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ValueMapping
import org.mapstruct.ValueMappings
import org.mapstruct.factory.Mappers
import org.voting.poll.adaptor.infrastructure.MappingConfiguration
import org.voting.poll.domain.poll.dto.AddQuestionDTO
import org.voting.poll.domain.poll.enums.QuestionType
import poll.Poll

@Mapper(config = MappingConfiguration::class)
interface AddQuestionMapper : BaseMapper {

    companion object {
        val mapper: AddQuestionMapper = Mappers.getMapper(AddQuestionMapper::class.java)
    }

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "role", expression = "java(mapRole(role))")
    @Mapping(target = "options", expression = "java(proto.getOptionsList())")
    @Mapping(target = "questionType", expression = "java(mapQuestionType(proto.getQuestionType()))")
    fun protoToDto(proto: Poll.AddQuestionRequest, role: String, userId: String): AddQuestionDTO


    @ValueMappings(
        ValueMapping(source = "EXPLAIN", target = "EXPLAIN"),
        ValueMapping(source = "TWO_OPTION", target = "TWO_OPTION"),
        ValueMapping(source = "MULTIPLE_CHOICE", target = "MULTIPLE_CHOICE"),
        ValueMapping(source = "UNRECOGNIZED", target = "EXPLAIN")
    )
    fun mapQuestionType(type: Poll.QuestionType): QuestionType
}



