//Generated by the protocol buffer compiler. DO NOT EDIT!
// source: provides/poll.proto

package org.voting.pollmanagement;

@kotlin.jvm.JvmName("-initializepollIdRequest")
inline fun pollIdRequest(block: org.voting.pollmanagement.PollIdRequestKt.Dsl.() -> kotlin.Unit): org.voting.pollmanagement.Poll.PollIdRequest =
  org.voting.pollmanagement.PollIdRequestKt.Dsl._create(org.voting.pollmanagement.Poll.PollIdRequest.newBuilder()).apply { block() }._build()
object PollIdRequestKt {
  @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
  @com.google.protobuf.kotlin.ProtoDslMarker
  class Dsl private constructor(
    private val _builder: org.voting.pollmanagement.Poll.PollIdRequest.Builder
  ) {
    companion object {
      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _create(builder: org.voting.pollmanagement.Poll.PollIdRequest.Builder): Dsl = Dsl(builder)
    }

    @kotlin.jvm.JvmSynthetic
    @kotlin.PublishedApi
    internal fun _build(): org.voting.pollmanagement.Poll.PollIdRequest = _builder.build()

    /**
     * <code>string id = 1;</code>
     */
    var id: kotlin.String
      @JvmName("getId")
      get() = _builder.getId()
      @JvmName("setId")
      set(value) {
        _builder.setId(value)
      }
    /**
     * <code>string id = 1;</code>
     */
    fun clearId() {
      _builder.clearId()
    }
  }
}
@kotlin.jvm.JvmSynthetic
inline fun org.voting.pollmanagement.Poll.PollIdRequest.copy(block: org.voting.pollmanagement.PollIdRequestKt.Dsl.() -> kotlin.Unit): org.voting.pollmanagement.Poll.PollIdRequest =
  org.voting.pollmanagement.PollIdRequestKt.Dsl._create(this.toBuilder()).apply { block() }._build()

