package org.voting.usermanagement.adaptor.rest.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import org.voting.usermanagement.adaptor.infrastructure.MappingConfiguration
import org.voting.usermanagement.adaptor.persistance.entity.Creator
import org.voting.usermanagement.adaptor.rest.dto.CreatorLoginResponse
import org.voting.usermanagement.domain.creator.CreatorModel


@Mapper(config = MappingConfiguration::class)
interface CreatorResponseMapper {

    companion object {
        val mapper: CreatorResponseMapper = Mappers.getMapper(CreatorResponseMapper::class.java)
    }

    fun modelToDto(model: CreatorModel, token: String): CreatorLoginResponse

}