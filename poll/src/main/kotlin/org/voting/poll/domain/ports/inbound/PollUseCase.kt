package org.voting.poll.domain.ports.inbound

import org.voting.poll.domain.poll.PollModel
import org.voting.poll.domain.poll.dto.CreatePollDTO

interface PollUseCase {
    fun createPoll(createPollDto: CreatePollDTO): PollModel

}
