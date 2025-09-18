package org.voting.poll.adaptor.api

import net.devh.boot.grpc.server.service.GrpcService
import org.voting.poll.adaptor.api.interceptors.UserInterceptor
import org.voting.poll.adaptor.api.mapper.ActivePollsMapper
import org.voting.poll.adaptor.api.mapper.AddQuestionMapper
import org.voting.poll.adaptor.api.mapper.CreatePollMapper
import org.voting.poll.adaptor.api.mapper.PollResponseMapper
import org.voting.poll.adaptor.api.mapper.UpdatePollMapper
import org.voting.poll.domain.poll.enums.Roles
import org.voting.poll.domain.ports.inbound.PollUseCase
import poll.Poll.AddQuestionRequest
import poll.Poll.CreatePollRequest
import poll.Poll.Empty
import poll.Poll.GetActivePollsResponse
import poll.Poll.PollInfo
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

    override suspend fun updatePoll(request: UpdatePollRequest): Empty {
        val userId = UserInterceptor.USER_ID_KEY.get()
        val role = UserInterceptor.ROLE_KEY.get()
        pollService.updatePoll(UpdatePollMapper.mapper.protoToDto(request, role, userId))
        return Empty.getDefaultInstance()
    }

    override suspend fun addQuestion(request: AddQuestionRequest): Empty {
        val userId = UserInterceptor.USER_ID_KEY.get()
        val role = UserInterceptor.ROLE_KEY.get()
        pollService.addQuestion(AddQuestionMapper.mapper.protoToDto(request, role, userId))
        return Empty.getDefaultInstance()
    }

    override suspend fun getActivePolls(request: Empty): GetActivePollsResponse {
        val userId = UserInterceptor.USER_ID_KEY.get()
        val role = UserInterceptor.ROLE_KEY.get()

        val convertedRole = try {
            Roles.valueOf(role)
        } catch (e: IllegalArgumentException) {
            null
        }

        val activePolls = pollService.getActivePolls(userId, convertedRole)

        val protoPolls: List<PollInfo> = ActivePollsMapper.MAPPER.toProtoList(activePolls)

        return GetActivePollsResponse.newBuilder()
            .addAllPolls(protoPolls)
            .build()
    }
}
