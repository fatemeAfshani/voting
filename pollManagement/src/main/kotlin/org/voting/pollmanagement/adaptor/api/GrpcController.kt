package org.voting.pollmanagement.adaptor.api

import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import org.voting.grpc.*
import org.voting.pollmanagement.application.service.PollService
import org.voting.pollmanagement.domain.model.Poll

@GrpcService
class GrpcController(private val pollService: PollService) : PollServiceGrpc.PollServiceImplBase() {

    override fun createPoll(request: PollRequest, responseObserver: StreamObserver<PollResponse>) {
        val poll = Poll(
            creatorId = request.creatorId,
            title = request.title,
            description = request.description,
            status = request.status.toEnum(),
            price = request.price,
            maxVoters = request.maxVoters,
            preferences = request.preferencesMap
        )
        val savedPoll = pollService.createPoll(poll)

        responseObserver.onNext(savedPoll.toGrpcResponse())
        responseObserver.onCompleted()
    }

    override fun getPollById(request: PollIdRequest, responseObserver: StreamObserver<PollResponse>) {
        val poll = pollService.getPollById(request.id)
        if (poll != null) {
            responseObserver.onNext(poll.toGrpcResponse())
        }
        responseObserver.onCompleted()
    }

    override fun getActivePolls(request: Empty, responseObserver: StreamObserver<PollListResponse>) {
        val activePolls = pollService.getActivePolls().map { it.toGrpcResponse() }
        responseObserver.onNext(PollListResponse.newBuilder().addAllPolls(activePolls).build())
        responseObserver.onCompleted()
    }

    override fun updatePoll(request: PollRequest, responseObserver: StreamObserver<PollResponse>) {
        val poll = Poll(
            id = request.id,
            creatorId = request.creatorId,
            title = request.title,
            description = request.description,
            status = request.status.toEnum(),
            price = request.price,
            maxVoters = request.maxVoters,
            preferences = request.preferencesMap
        )
        val updatedPoll = pollService.updatePoll(poll)

        responseObserver.onNext(updatedPoll.toGrpcResponse())
        responseObserver.onCompleted()
    }
}

// Extension functions to convert between domain and gRPC models
fun Poll.toGrpcResponse(): PollResponse = PollResponse.newBuilder()
    .setId(id ?: "")
    .setCreatorId(creatorId)
    .setTitle(title)
    .setDescription(description ?: "")
    .setStatus(status.name)
    .setPrice(price)
    .setMaxVoters(maxVoters)
    .putAllPreferences(preferences)
    .build()
