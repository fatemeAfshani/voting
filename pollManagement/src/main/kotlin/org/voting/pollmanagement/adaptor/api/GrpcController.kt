package org.voting.pollmanagement.adaptor.api


import com.google.protobuf.Empty
import net.devh.boot.grpc.server.service.GrpcService
import org.voting.poll.*
import org.voting.pollmanagement.adaptor.api.interceptors.UserInterceptor
import org.voting.pollmanagement.adaptor.api.mapper.CreatePollMapper
import org.voting.pollmanagement.domain.ports.inbound.PollUseCase


@GrpcService
class GrpcController(
private val pollService: PollUseCase
) : PollServiceGrpcKt.PollServiceCoroutineImplBase() {
    override suspend fun createPoll(request: CreatePollRequest): Empty {
        val userId = UserInterceptor.USER_ID_KEY.get()
        val role = UserInterceptor.ROLE_KEY.get()
         pollService.createPoll(CreatePollMapper.mapper.protoToDto(request, role, userId))
        return Empty.getDefaultInstance()

    }


}

//show price of the poll to user when trying to set max voters and preferences
//in see poll status, show shallow detail of the poll
