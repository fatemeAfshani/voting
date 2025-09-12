package org.voting.user.adaptor.persistance.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import org.voting.user.adaptor.infrastructure.MappingConfiguration
import org.voting.user.adaptor.persistance.entity.Voter
import org.voting.user.domain.voter.VoterModel

@Mapper(config = MappingConfiguration::class)
interface VoterMapper {
    companion object {
        val mapper: VoterMapper = Mappers.getMapper(VoterMapper::class.java)
    }

    fun entityToModel(entity: Voter): VoterModel
    fun modelToEntity(model: VoterModel): Voter
}
