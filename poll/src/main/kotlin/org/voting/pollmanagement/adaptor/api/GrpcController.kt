package org.voting.pollmanagement.adaptor.api


import net.devh.boot.grpc.server.service.GrpcService
import org.voting.pollmanagement.adaptor.api.interceptors.UserInterceptor
import org.voting.pollmanagement.adaptor.api.mapper.CreatePollMapper
import org.voting.pollmanagement.adaptor.api.mapper.PollResponseMapper
import org.voting.pollmanagement.domain.ports.inbound.PollUseCase
import pollManagement.Poll.CreatePollRequest
import pollManagement.Poll.PollResponse
import pollManagement.PollServiceGrpcKt


@GrpcService
class GrpcController(
private val pollService: PollUseCase
) : PollServiceGrpcKt.PollServiceCoroutineImplBase() {
    override suspend fun createPoll(request: CreatePollRequest): PollResponse {
        val userId = UserInterceptor.USER_ID_KEY.get()
        val role = UserInterceptor.ROLE_KEY.get()
         val createdPoll = pollService.createPoll(CreatePollMapper.mapper.protoToDto(request, role, userId))
        return PollResponseMapper.mapper.dtoToProto(createdPoll)

    }
}

//show price of the poll to user when trying to set max voters and preferences
//in see poll status, show shallow detail of the poll
