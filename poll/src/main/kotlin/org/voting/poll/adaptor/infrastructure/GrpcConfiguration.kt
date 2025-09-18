package org.voting.poll.adaptor.infrastructure

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import net.devh.boot.grpc.server.serverfactory.GrpcServerConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.Executors
import user.VoterServiceGrpcKt



@Configuration
class GrpcConfiguration {
    @Bean
    fun grpcServerConfigurer(): GrpcServerConfigurer {
        return GrpcServerConfigurer { serverBuilder ->
            serverBuilder
                .executor(Executors.newFixedThreadPool(8))
                .maxInboundMessageSize(16 * 1024 * 1024) // 16MB
        }
    }

    @Bean
    fun userServiceChannel(): ManagedChannel {
        return ManagedChannelBuilder
            .forAddress("localhost", 9090)
            .usePlaintext()
            .build()
    }

    @Bean
    fun voterServiceStub(channel: ManagedChannel): VoterServiceGrpcKt.VoterServiceCoroutineStub {
        return VoterServiceGrpcKt.VoterServiceCoroutineStub(channel)
    }
}
