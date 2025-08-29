package org.voting.user.adaptor.persistance.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import org.voting.user.adaptor.infrastructure.MappingConfiguration
import org.voting.user.adaptor.persistance.entity.Creator
import org.voting.user.domain.creator.CreatorModel


@Mapper(config = MappingConfiguration::class)
interface CreatorMapper {

    companion object {
        val mapper: CreatorMapper = Mappers.getMapper(CreatorMapper::class.java)
    }

    fun modelToEntity(model: CreatorModel?): Creator?

    fun entityToModel(entity: Creator?): CreatorModel?
}
