package org.voting.usermanagement.adaptor.infrastructure


import org.mapstruct.InjectionStrategy
import org.mapstruct.MapperConfig
import org.mapstruct.extensions.spring.SpringMapperConfig

@MapperConfig(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@SpringMapperConfig
interface MappingConfiguration