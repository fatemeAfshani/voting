package org.voting.poll.integrationTests

import io.grpc.Metadata
import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import io.grpc.stub.MetadataUtils
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.voting.poll.adaptor.api.GrpcController
import org.voting.poll.adaptor.api.JwtUtil
import org.voting.poll.adaptor.api.interceptors.UserInterceptor
import org.voting.poll.adaptor.api.mapper.CreatePollMapper
import org.voting.poll.adaptor.persistance.repository.MongoPollRepository
import org.voting.poll.domain.poll.enums.PollStatus
import org.voting.poll.domain.ports.inbound.PollUseCase
import org.voting.poll.shared.Fixtures
import poll.Poll
import poll.Poll.CreatePollRequest
import poll.PollServiceGrpcKt
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@Testcontainers
@SpringBootTest
@ExtendWith(MockitoExtension::class)
class PollIntegrationTest {

    @Autowired
    private lateinit var pollService: PollUseCase

    @Autowired
    private lateinit var mongoPollRepository: MongoPollRepository

    companion object {
        @Container
        val mongoDBContainer: MongoDBContainer = MongoDBContainer("mongo:latest")
            .withExposedPorts(27017)

        init {
            mongoDBContainer.start()
            System.setProperty("spring.data.mongodb.uri", mongoDBContainer.replicaSetUrl)
        }
    }

    @BeforeEach
    fun setup() {
        mongoPollRepository.deleteAll()
    }

    @Test
    fun `createPoll should work via gRPC call`(): Unit = runBlocking {


        val request = CreatePollRequest.newBuilder()
            .setTitle("Test Poll Title")
            .setDescription("Test Poll Description")
            .build()

        val createdPoll = pollService.createPoll(
            CreatePollMapper.mapper.protoToDto(request,Fixtures.mockCreatorRole, Fixtures.mockCreatorId )
        )

        assertNotNull(createdPoll.id)
        val data = mongoPollRepository.findOneById(createdPoll.id!!)
        assertEquals(data?.creatorId, Fixtures.mockCreatorId)
        assertEquals(data?.status, PollStatus.DRAFT)

    }
}
