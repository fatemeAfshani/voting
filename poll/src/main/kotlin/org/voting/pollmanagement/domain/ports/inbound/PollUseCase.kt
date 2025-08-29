package org.voting.pollmanagement.domain.ports.inbound

import org.voting.pollmanagement.domain.poll.PollModel
import org.voting.pollmanagement.domain.poll.dto.CreatePollDTO

interface PollUseCase {
    fun createPoll(createPollDto: CreatePollDTO): PollModel

}
