package org.voting.poll.domain.poll

import org.springframework.stereotype.Service
import org.voting.poll.adaptor.exception.Errors
import org.voting.poll.adaptor.exception.UnknownException
import org.voting.poll.domain.poll.dto.CreatePollDTO
import org.voting.poll.domain.ports.inbound.PollUseCase
import org.voting.poll.domain.ports.outbound.persistance.PollRepository

@Service
class PollService(private val pollRepository: PollRepository) : PollUseCase {

    override fun createPoll(createPollDto: CreatePollDTO): PollModel {
        val (title, description, userId) =  createPollDto
        val poll = PollModel(title = title, description = description, creatorId = userId)
        // todo: check if there is unpaid poll, do not create new one
        val createdPoll = pollRepository.insert(poll) ?: throw UnknownException(Errors.ErrorCodes.UNKNOWN_ERROR.name)
        return createdPoll
    }
}
