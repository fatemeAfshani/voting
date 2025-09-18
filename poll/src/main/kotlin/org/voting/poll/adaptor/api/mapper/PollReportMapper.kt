package org.voting.poll.adaptor.api.mapper

import com.google.protobuf.ByteString
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import org.voting.poll.adaptor.infrastructure.MappingConfiguration
import org.voting.poll.domain.poll.dto.PollReportDTO
import org.voting.poll.domain.poll.dto.PollReportResponseDTO
import poll.Poll.PollReportRequest
import poll.Poll.PollReportResponse

@Mapper(config = MappingConfiguration::class, imports = [ByteString::class])
interface PollReportMapper : BaseMapper {
    companion object {
        val mapper: PollReportMapper = Mappers.getMapper(PollReportMapper::class.java)
    }

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "role", expression = "java(mapRole(role))")
    fun protoToDto(proto: PollReportRequest, role: String, userId: String): PollReportDTO

    @Mapping(target = "excelFile", expression = "java(ByteString.copyFrom(dto.getExcelFile()))")
    fun dtoToProto(dto: PollReportResponseDTO): PollReportResponse
}
