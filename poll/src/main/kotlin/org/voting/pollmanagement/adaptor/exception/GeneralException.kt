package org.voting.pollmanagement.adaptor.exception

import io.grpc.Status


abstract class GeneralException(message: String) : RuntimeException(message)
