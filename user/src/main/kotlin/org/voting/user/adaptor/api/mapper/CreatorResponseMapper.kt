package org.voting.user.adaptor.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import org.voting.user.adaptor.infrastructure.MappingConfiguration
import org.voting.user.adaptor.api.dto.CreatorLoginResponse
import org.voting.user.domain.creator.CreatorModel


@Mapper(config = MappingConfiguration::class)
interface CreatorResponseMapper {

    companion object {
        val mapper: CreatorResponseMapper = Mappers.getMapper(CreatorResponseMapper::class.java)
    }

    @Mapping(source = "token", target = "token")
    @Mapping(source = "model.id", target = "id")
    fun modelToDto(model: CreatorModel, token: String): CreatorLoginResponse

}
