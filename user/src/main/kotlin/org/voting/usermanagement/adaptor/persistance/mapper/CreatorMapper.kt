package org.voting.usermanagement.adaptor.persistance.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import org.voting.usermanagement.adaptor.infrastructure.MappingConfiguration
import org.voting.usermanagement.adaptor.persistance.entity.Creator
import org.voting.usermanagement.domain.creator.CreatorModel


@Mapper(config = MappingConfiguration::class)
interface CreatorMapper {

    companion object {
        val mapper: CreatorMapper = Mappers.getMapper(CreatorMapper::class.java)
    }

    fun modelToEntity(model: CreatorModel?): Creator?

    fun entityToModel(entity: Creator?): CreatorModel?
}