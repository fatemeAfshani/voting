package org.voting.pollmanagement.adaptor.api


import com.google.protobuf.Empty
import net.devh.boot.grpc.server.service.GrpcService
import org.voting.poll.*


@GrpcService
class GrpcController : PollServiceGrpcKt.PollServiceCoroutineImplBase() {
    override suspend fun createPoll(request: PollRequest): PollResponse {
        return PollResponse.getDefaultInstance()
    }

    override suspend fun getActivePolls(request: Empty): PollListResponse {
        return super.getActivePolls(request)
    }

    override suspend fun getPollById(request: PollIdRequest): PollResponse {
        return super.getPollById(request)
    }

    override suspend fun updatePoll(request: PollRequest): PollResponse {
        return super.updatePoll(request)
    }
}