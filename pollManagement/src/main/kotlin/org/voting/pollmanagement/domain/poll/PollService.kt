package org.voting.pollmanagement.application.service

import org.springframework.stereotype.Service
import org.voting.pollmanagement.domain.poll.PollModel
import org.voting.pollmanagement.domain.poll.dto.CreatePollDTO
import org.voting.pollmanagement.domain.ports.inbound.PollUseCase
import org.voting.pollmanagement.domain.ports.outbound.persistance.PollRepository

@Service
class PollService(private val pollRepository: PollRepository) : PollUseCase {

    override fun createPoll(createPollDto: CreatePollDTO): PollModel? {
        val (title, description, userId) =  createPollDto
        val poll = PollModel(title = title, description = description, creatorId = userId)
        return pollRepository.save(poll)
    }


}
