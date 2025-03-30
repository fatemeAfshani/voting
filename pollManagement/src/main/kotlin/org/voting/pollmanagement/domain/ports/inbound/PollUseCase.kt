package org.voting.pollmanagement.domain.ports.inbound

import org.voting.pollmanagement.domain.poll.PollModel

interface PollUseCase {
    fun createPoll(poll: PollModel): PollModel?
    fun getPollById(id: String): PollModel?
    fun getActivePolls(): List<PollModel?>
    fun updatePoll(poll: PollModel): PollModel?
}
