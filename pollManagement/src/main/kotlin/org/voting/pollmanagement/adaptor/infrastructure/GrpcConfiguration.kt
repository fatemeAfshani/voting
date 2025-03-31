package org.voting.pollmanagement.adaptor.infrastructure

import net.devh.boot.grpc.server.serverfactory.GrpcServerConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.Executors


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
}