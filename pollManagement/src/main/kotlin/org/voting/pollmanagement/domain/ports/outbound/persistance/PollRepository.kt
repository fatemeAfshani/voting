package org.voting.pollmanagement.domain.ports.outbound.persistance

import org.voting.pollmanagement.domain.poll.PollModel
import org.voting.pollmanagement.domain.poll.enums.PollStatus

interface PollRepository {
    fun save(poll: PollModel): PollModel?
    fun findById(id: String): PollModel?
    fun findByStatus(status: PollStatus): List<PollModel?>
}
