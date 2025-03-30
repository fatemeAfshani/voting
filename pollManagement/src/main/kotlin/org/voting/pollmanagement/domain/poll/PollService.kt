package org.voting.pollmanagement.application.service

import org.springframework.stereotype.Service
import org.voting.pollmanagement.domain.poll.PollModel
import org.voting.pollmanagement.domain.poll.enums.PollStatus
import org.voting.pollmanagement.domain.ports.inbound.PollUseCase
import org.voting.pollmanagement.domain.ports.outbound.persistance.PollRepository

@Service
class PollService(private val pollRepository: PollRepository) : PollUseCase {

    override fun createPoll(poll: PollModel): PollModel? {
        return pollRepository.save(poll)
    }

    override fun getPollById(id: String): PollModel? {
        return pollRepository.findById(id)
    }

    override fun getActivePolls(): List<PollModel?> {
        return pollRepository.findByStatus(PollStatus.ACTIVE)
    }

    override fun updatePoll(poll: PollModel): PollModel? {
        return pollRepository.save(poll)
    }
}
