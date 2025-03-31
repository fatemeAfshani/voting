package org.voting.pollmanagement

import com.google.protobuf.Empty
import io.grpc.CallOptions
import io.grpc.CallOptions.DEFAULT
import io.grpc.Channel
import io.grpc.Metadata
import io.grpc.MethodDescriptor
import io.grpc.ServerServiceDefinition
import io.grpc.ServerServiceDefinition.builder
import io.grpc.ServiceDescriptor
import io.grpc.Status
import io.grpc.Status.UNIMPLEMENTED
import io.grpc.StatusException
import io.grpc.kotlin.AbstractCoroutineServerImpl
import io.grpc.kotlin.AbstractCoroutineStub
import io.grpc.kotlin.ClientCalls
import io.grpc.kotlin.ClientCalls.unaryRpc
import io.grpc.kotlin.ServerCalls
import io.grpc.kotlin.ServerCalls.unaryServerMethodDefinition
import io.grpc.kotlin.StubFor
import kotlin.String
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic
import org.voting.pollmanagement.PollServiceGrpc.getServiceDescriptor

/**
 * Holder for Kotlin coroutine-based client and server APIs for
 * org.voting.pollmanagement.PollService.
 */
public object PollServiceGrpcKt {
  public const val SERVICE_NAME: String = PollServiceGrpc.SERVICE_NAME

  @JvmStatic
  public val serviceDescriptor: ServiceDescriptor
    get() = PollServiceGrpc.getServiceDescriptor()

  public val createPollMethod: MethodDescriptor<Poll.PollRequest, Poll.PollResponse>
    @JvmStatic
    get() = PollServiceGrpc.getCreatePollMethod()

  public val getPollByIdMethod: MethodDescriptor<Poll.PollIdRequest, Poll.PollResponse>
    @JvmStatic
    get() = PollServiceGrpc.getGetPollByIdMethod()

  public val getActivePollsMethod: MethodDescriptor<Empty, Poll.PollListResponse>
    @JvmStatic
    get() = PollServiceGrpc.getGetActivePollsMethod()

  public val updatePollMethod: MethodDescriptor<Poll.PollRequest, Poll.PollResponse>
    @JvmStatic
    get() = PollServiceGrpc.getUpdatePollMethod()

  /**
   * A stub for issuing RPCs to a(n) org.voting.pollmanagement.PollService service as suspending
   * coroutines.
   */
  @StubFor(PollServiceGrpc::class)
  public class PollServiceCoroutineStub @JvmOverloads constructor(
    channel: Channel,
    callOptions: CallOptions = DEFAULT,
  ) : AbstractCoroutineStub<PollServiceCoroutineStub>(channel, callOptions) {
    public override fun build(channel: Channel, callOptions: CallOptions): PollServiceCoroutineStub
        = PollServiceCoroutineStub(channel, callOptions)

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun createPoll(request: Poll.PollRequest, headers: Metadata = Metadata()):
        Poll.PollResponse = unaryRpc(
      channel,
      PollServiceGrpc.getCreatePollMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun getPollById(request: Poll.PollIdRequest, headers: Metadata = Metadata()):
        Poll.PollResponse = unaryRpc(
      channel,
      PollServiceGrpc.getGetPollByIdMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun getActivePolls(request: Empty, headers: Metadata = Metadata()):
        Poll.PollListResponse = unaryRpc(
      channel,
      PollServiceGrpc.getGetActivePollsMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun updatePoll(request: Poll.PollRequest, headers: Metadata = Metadata()):
        Poll.PollResponse = unaryRpc(
      channel,
      PollServiceGrpc.getUpdatePollMethod(),
      request,
      callOptions,
      headers
    )
  }

  /**
   * Skeletal implementation of the org.voting.pollmanagement.PollService service based on Kotlin
   * coroutines.
   */
  public abstract class PollServiceCoroutineImplBase(
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
  ) : AbstractCoroutineServerImpl(coroutineContext) {
    /**
     * Returns the response to an RPC for org.voting.pollmanagement.PollService.CreatePoll.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun createPoll(request: Poll.PollRequest): Poll.PollResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method org.voting.pollmanagement.PollService.CreatePoll is unimplemented"))

    /**
     * Returns the response to an RPC for org.voting.pollmanagement.PollService.GetPollById.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun getPollById(request: Poll.PollIdRequest): Poll.PollResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method org.voting.pollmanagement.PollService.GetPollById is unimplemented"))

    /**
     * Returns the response to an RPC for org.voting.pollmanagement.PollService.GetActivePolls.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun getActivePolls(request: Empty): Poll.PollListResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method org.voting.pollmanagement.PollService.GetActivePolls is unimplemented"))

    /**
     * Returns the response to an RPC for org.voting.pollmanagement.PollService.UpdatePoll.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun updatePoll(request: Poll.PollRequest): Poll.PollResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method org.voting.pollmanagement.PollService.UpdatePoll is unimplemented"))

    public final override fun bindService(): ServerServiceDefinition =
        builder(getServiceDescriptor())
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = PollServiceGrpc.getCreatePollMethod(),
      implementation = ::createPoll
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = PollServiceGrpc.getGetPollByIdMethod(),
      implementation = ::getPollById
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = PollServiceGrpc.getGetActivePollsMethod(),
      implementation = ::getActivePolls
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = PollServiceGrpc.getUpdatePollMethod(),
      implementation = ::updatePoll
    )).build()
  }
}
