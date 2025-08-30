package org.voting.poll.adaptor.api


import net.devh.boot.grpc.server.service.GrpcService
import org.voting.poll.adaptor.api.interceptors.UserInterceptor
import org.voting.poll.adaptor.api.mapper.CreatePollMapper
import org.voting.poll.adaptor.api.mapper.PollResponseMapper
import org.voting.poll.adaptor.api.mapper.UpdatePollMapper
import org.voting.poll.domain.ports.inbound.PollUseCase
import poll.Poll.CreatePollRequest
import poll.Poll.EmptyResponse
import poll.Poll.PollResponse
import poll.Poll.UpdatePollRequest
import poll.PollServiceGrpcKt


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

    override suspend fun updatePoll(request: UpdatePollRequest): EmptyResponse {
        val userId = UserInterceptor.USER_ID_KEY.get()
        val role = UserInterceptor.ROLE_KEY.get()
        pollService.updatePoll(UpdatePollMapper.mapper.protoToDto(request, role, userId))
        return EmptyResponse.getDefaultInstance()

    }
}

//show price of the poll to user when trying to set max voters and preferences
//in see poll status, show shallow detail of the poll
