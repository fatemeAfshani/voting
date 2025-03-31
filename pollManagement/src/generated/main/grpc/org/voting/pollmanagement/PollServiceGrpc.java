package org.voting.pollmanagement;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.50.2)",
    comments = "Source: provides/poll.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class PollServiceGrpc {

  private PollServiceGrpc() {}

  public static final String SERVICE_NAME = "org.voting.pollmanagement.PollService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.voting.pollmanagement.Poll.PollRequest,
      org.voting.pollmanagement.Poll.PollResponse> getCreatePollMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreatePoll",
      requestType = org.voting.pollmanagement.Poll.PollRequest.class,
      responseType = org.voting.pollmanagement.Poll.PollResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.voting.pollmanagement.Poll.PollRequest,
      org.voting.pollmanagement.Poll.PollResponse> getCreatePollMethod() {
    io.grpc.MethodDescriptor<org.voting.pollmanagement.Poll.PollRequest, org.voting.pollmanagement.Poll.PollResponse> getCreatePollMethod;
    if ((getCreatePollMethod = PollServiceGrpc.getCreatePollMethod) == null) {
      synchronized (PollServiceGrpc.class) {
        if ((getCreatePollMethod = PollServiceGrpc.getCreatePollMethod) == null) {
          PollServiceGrpc.getCreatePollMethod = getCreatePollMethod =
              io.grpc.MethodDescriptor.<org.voting.pollmanagement.Poll.PollRequest, org.voting.pollmanagement.Poll.PollResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreatePoll"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.voting.pollmanagement.Poll.PollRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.voting.pollmanagement.Poll.PollResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PollServiceMethodDescriptorSupplier("CreatePoll"))
              .build();
        }
      }
    }
    return getCreatePollMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.voting.pollmanagement.Poll.PollIdRequest,
      org.voting.pollmanagement.Poll.PollResponse> getGetPollByIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetPollById",
      requestType = org.voting.pollmanagement.Poll.PollIdRequest.class,
      responseType = org.voting.pollmanagement.Poll.PollResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.voting.pollmanagement.Poll.PollIdRequest,
      org.voting.pollmanagement.Poll.PollResponse> getGetPollByIdMethod() {
    io.grpc.MethodDescriptor<org.voting.pollmanagement.Poll.PollIdRequest, org.voting.pollmanagement.Poll.PollResponse> getGetPollByIdMethod;
    if ((getGetPollByIdMethod = PollServiceGrpc.getGetPollByIdMethod) == null) {
      synchronized (PollServiceGrpc.class) {
        if ((getGetPollByIdMethod = PollServiceGrpc.getGetPollByIdMethod) == null) {
          PollServiceGrpc.getGetPollByIdMethod = getGetPollByIdMethod =
              io.grpc.MethodDescriptor.<org.voting.pollmanagement.Poll.PollIdRequest, org.voting.pollmanagement.Poll.PollResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetPollById"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.voting.pollmanagement.Poll.PollIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.voting.pollmanagement.Poll.PollResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PollServiceMethodDescriptorSupplier("GetPollById"))
              .build();
        }
      }
    }
    return getGetPollByIdMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      org.voting.pollmanagement.Poll.PollListResponse> getGetActivePollsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetActivePolls",
      requestType = com.google.protobuf.Empty.class,
      responseType = org.voting.pollmanagement.Poll.PollListResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      org.voting.pollmanagement.Poll.PollListResponse> getGetActivePollsMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, org.voting.pollmanagement.Poll.PollListResponse> getGetActivePollsMethod;
    if ((getGetActivePollsMethod = PollServiceGrpc.getGetActivePollsMethod) == null) {
      synchronized (PollServiceGrpc.class) {
        if ((getGetActivePollsMethod = PollServiceGrpc.getGetActivePollsMethod) == null) {
          PollServiceGrpc.getGetActivePollsMethod = getGetActivePollsMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, org.voting.pollmanagement.Poll.PollListResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetActivePolls"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.voting.pollmanagement.Poll.PollListResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PollServiceMethodDescriptorSupplier("GetActivePolls"))
              .build();
        }
      }
    }
    return getGetActivePollsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.voting.pollmanagement.Poll.PollRequest,
      org.voting.pollmanagement.Poll.PollResponse> getUpdatePollMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdatePoll",
      requestType = org.voting.pollmanagement.Poll.PollRequest.class,
      responseType = org.voting.pollmanagement.Poll.PollResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.voting.pollmanagement.Poll.PollRequest,
      org.voting.pollmanagement.Poll.PollResponse> getUpdatePollMethod() {
    io.grpc.MethodDescriptor<org.voting.pollmanagement.Poll.PollRequest, org.voting.pollmanagement.Poll.PollResponse> getUpdatePollMethod;
    if ((getUpdatePollMethod = PollServiceGrpc.getUpdatePollMethod) == null) {
      synchronized (PollServiceGrpc.class) {
        if ((getUpdatePollMethod = PollServiceGrpc.getUpdatePollMethod) == null) {
          PollServiceGrpc.getUpdatePollMethod = getUpdatePollMethod =
              io.grpc.MethodDescriptor.<org.voting.pollmanagement.Poll.PollRequest, org.voting.pollmanagement.Poll.PollResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdatePoll"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.voting.pollmanagement.Poll.PollRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.voting.pollmanagement.Poll.PollResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PollServiceMethodDescriptorSupplier("UpdatePoll"))
              .build();
        }
      }
    }
    return getUpdatePollMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static PollServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PollServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PollServiceStub>() {
        @java.lang.Override
        public PollServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PollServiceStub(channel, callOptions);
        }
      };
    return PollServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static PollServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PollServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PollServiceBlockingStub>() {
        @java.lang.Override
        public PollServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PollServiceBlockingStub(channel, callOptions);
        }
      };
    return PollServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static PollServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PollServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PollServiceFutureStub>() {
        @java.lang.Override
        public PollServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PollServiceFutureStub(channel, callOptions);
        }
      };
    return PollServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class PollServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void createPoll(org.voting.pollmanagement.Poll.PollRequest request,
        io.grpc.stub.StreamObserver<org.voting.pollmanagement.Poll.PollResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreatePollMethod(), responseObserver);
    }

    /**
     */
    public void getPollById(org.voting.pollmanagement.Poll.PollIdRequest request,
        io.grpc.stub.StreamObserver<org.voting.pollmanagement.Poll.PollResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetPollByIdMethod(), responseObserver);
    }

    /**
     */
    public void getActivePolls(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<org.voting.pollmanagement.Poll.PollListResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetActivePollsMethod(), responseObserver);
    }

    /**
     */
    public void updatePoll(org.voting.pollmanagement.Poll.PollRequest request,
        io.grpc.stub.StreamObserver<org.voting.pollmanagement.Poll.PollResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdatePollMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getCreatePollMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                org.voting.pollmanagement.Poll.PollRequest,
                org.voting.pollmanagement.Poll.PollResponse>(
                  this, METHODID_CREATE_POLL)))
          .addMethod(
            getGetPollByIdMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                org.voting.pollmanagement.Poll.PollIdRequest,
                org.voting.pollmanagement.Poll.PollResponse>(
                  this, METHODID_GET_POLL_BY_ID)))
          .addMethod(
            getGetActivePollsMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.protobuf.Empty,
                org.voting.pollmanagement.Poll.PollListResponse>(
                  this, METHODID_GET_ACTIVE_POLLS)))
          .addMethod(
            getUpdatePollMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                org.voting.pollmanagement.Poll.PollRequest,
                org.voting.pollmanagement.Poll.PollResponse>(
                  this, METHODID_UPDATE_POLL)))
          .build();
    }
  }

  /**
   */
  public static final class PollServiceStub extends io.grpc.stub.AbstractAsyncStub<PollServiceStub> {
    private PollServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PollServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PollServiceStub(channel, callOptions);
    }

    /**
     */
    public void createPoll(org.voting.pollmanagement.Poll.PollRequest request,
        io.grpc.stub.StreamObserver<org.voting.pollmanagement.Poll.PollResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreatePollMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getPollById(org.voting.pollmanagement.Poll.PollIdRequest request,
        io.grpc.stub.StreamObserver<org.voting.pollmanagement.Poll.PollResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetPollByIdMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getActivePolls(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<org.voting.pollmanagement.Poll.PollListResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetActivePollsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updatePoll(org.voting.pollmanagement.Poll.PollRequest request,
        io.grpc.stub.StreamObserver<org.voting.pollmanagement.Poll.PollResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdatePollMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class PollServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<PollServiceBlockingStub> {
    private PollServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PollServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PollServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public org.voting.pollmanagement.Poll.PollResponse createPoll(org.voting.pollmanagement.Poll.PollRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreatePollMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.voting.pollmanagement.Poll.PollResponse getPollById(org.voting.pollmanagement.Poll.PollIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetPollByIdMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.voting.pollmanagement.Poll.PollListResponse getActivePolls(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetActivePollsMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.voting.pollmanagement.Poll.PollResponse updatePoll(org.voting.pollmanagement.Poll.PollRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdatePollMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class PollServiceFutureStub extends io.grpc.stub.AbstractFutureStub<PollServiceFutureStub> {
    private PollServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PollServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PollServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.voting.pollmanagement.Poll.PollResponse> createPoll(
        org.voting.pollmanagement.Poll.PollRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreatePollMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.voting.pollmanagement.Poll.PollResponse> getPollById(
        org.voting.pollmanagement.Poll.PollIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetPollByIdMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.voting.pollmanagement.Poll.PollListResponse> getActivePolls(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetActivePollsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.voting.pollmanagement.Poll.PollResponse> updatePoll(
        org.voting.pollmanagement.Poll.PollRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdatePollMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_POLL = 0;
  private static final int METHODID_GET_POLL_BY_ID = 1;
  private static final int METHODID_GET_ACTIVE_POLLS = 2;
  private static final int METHODID_UPDATE_POLL = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final PollServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(PollServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CREATE_POLL:
          serviceImpl.createPoll((org.voting.pollmanagement.Poll.PollRequest) request,
              (io.grpc.stub.StreamObserver<org.voting.pollmanagement.Poll.PollResponse>) responseObserver);
          break;
        case METHODID_GET_POLL_BY_ID:
          serviceImpl.getPollById((org.voting.pollmanagement.Poll.PollIdRequest) request,
              (io.grpc.stub.StreamObserver<org.voting.pollmanagement.Poll.PollResponse>) responseObserver);
          break;
        case METHODID_GET_ACTIVE_POLLS:
          serviceImpl.getActivePolls((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<org.voting.pollmanagement.Poll.PollListResponse>) responseObserver);
          break;
        case METHODID_UPDATE_POLL:
          serviceImpl.updatePoll((org.voting.pollmanagement.Poll.PollRequest) request,
              (io.grpc.stub.StreamObserver<org.voting.pollmanagement.Poll.PollResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class PollServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    PollServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.voting.pollmanagement.Poll.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("PollService");
    }
  }

  private static final class PollServiceFileDescriptorSupplier
      extends PollServiceBaseDescriptorSupplier {
    PollServiceFileDescriptorSupplier() {}
  }

  private static final class PollServiceMethodDescriptorSupplier
      extends PollServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    PollServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (PollServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new PollServiceFileDescriptorSupplier())
              .addMethod(getCreatePollMethod())
              .addMethod(getGetPollByIdMethod())
              .addMethod(getGetActivePollsMethod())
              .addMethod(getUpdatePollMethod())
              .build();
        }
      }
    }
    return result;
  }
}
