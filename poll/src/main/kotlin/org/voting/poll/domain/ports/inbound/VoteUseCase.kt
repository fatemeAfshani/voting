package org.voting.poll.domain.ports.inbound

import org.voting.poll.domain.vote.dto.StartVoteDTO
import org.voting.poll.domain.vote.dto.StartVoteResponse

interface VoteUseCase {

    fun startVoting(data: StartVoteDTO): StartVoteResponse
}
