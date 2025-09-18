package org.voting.poll.adaptor.api

import net.devh.boot.grpc.server.service.GrpcService
import org.voting.poll.adaptor.api.interceptors.UserInterceptor
import org.voting.poll.adaptor.api.mapper.*
import org.voting.poll.domain.poll.enums.Roles
import org.voting.poll.domain.ports.inbound.PollUseCase
import org.voting.poll.domain.ports.inbound.VoteUseCase
import poll.Poll.AddQuestionRequest
import poll.Poll.AnswerQuestionRequest
import poll.Poll.AnswerQuestionResponse
import poll.Poll.CreatePollRequest
import poll.Poll.Empty
import poll.Poll.GetActivePollsResponse
import poll.Poll.PollInfo
import poll.Poll.PollResponse
import poll.Poll.StartVotingRequest
import poll.Poll.StartVotingResponse
import poll.Poll.UpdatePollRequest
import poll.PollServiceGrpcKt

@GrpcService
class GrpcController(
    private val pollService: PollUseCase,
    private val voteService: VoteUseCase,
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

    override suspend fun startVoting(request: StartVotingRequest): StartVotingResponse {
        val userId = UserInterceptor.USER_ID_KEY.get()
        val role = UserInterceptor.ROLE_KEY.get()

        val voteResult = voteService.startVoting(
            StartVotingMapper.mapper.protoToDto(request, role, userId)
        )

        val firstQuestion = voteResult.firstQuestion
        val firstQuestionProto = createQuestionProto(firstQuestion, voteResult.pollId)

        return StartVotingResponse.newBuilder()
            .apply {
                voteResult.currentAnswer?.let { setCurrentAnswer(it) }
                setFirstQuestion(firstQuestionProto)
            }
            .build()
    }

    override suspend fun answerQuestion(request: AnswerQuestionRequest): AnswerQuestionResponse {
        val userId = UserInterceptor.USER_ID_KEY.get()
        val role = UserInterceptor.ROLE_KEY.get()

        val result = voteService.answerQuestion(AnswerQuestionMapper.mapper.protoToDto(request, role, userId))

        val nextQ = createQuestionProto(result.nextQuestion, result.pollId)
        val prevQ = createQuestionProto(result.previousQuestion, result.pollId)

        return AnswerQuestionResponse.newBuilder()
            .setNextQuestion(nextQ)
            .setPreviousQuestion(prevQ)
            .apply {
                result.nextQuestionAnswer?.let { setNextAnswer(it) }
                result.previousQuestionAnswer?.let { setPreviousAnswer(it) }
            }
            .build()
    }

    override suspend fun getReport(request: Poll.PollReportRequest): Poll.PollReportResponse {
        val userId = UserInterceptor.USER_ID_KEY.get()
        val role = UserInterceptor.ROLE_KEY.get()

        val result = pollService.getReport(PollReportMapper.mapper.protoToDto(request, role, userId))

        return PollReportMapper.mapper.dtoToProto(result)
    }
}
