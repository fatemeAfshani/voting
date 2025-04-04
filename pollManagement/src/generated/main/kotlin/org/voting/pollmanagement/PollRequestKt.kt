//Generated by the protocol buffer compiler. DO NOT EDIT!
// source: provides/poll.proto

package org.voting.pollmanagement;

@kotlin.jvm.JvmName("-initializepollRequest")
inline fun pollRequest(block: org.voting.pollmanagement.PollRequestKt.Dsl.() -> kotlin.Unit): org.voting.pollmanagement.Poll.PollRequest =
  org.voting.pollmanagement.PollRequestKt.Dsl._create(org.voting.pollmanagement.Poll.PollRequest.newBuilder()).apply { block() }._build()
object PollRequestKt {
  @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
  @com.google.protobuf.kotlin.ProtoDslMarker
  class Dsl private constructor(
    private val _builder: org.voting.pollmanagement.Poll.PollRequest.Builder
  ) {
    companion object {
      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _create(builder: org.voting.pollmanagement.Poll.PollRequest.Builder): Dsl = Dsl(builder)
    }

    @kotlin.jvm.JvmSynthetic
    @kotlin.PublishedApi
    internal fun _build(): org.voting.pollmanagement.Poll.PollRequest = _builder.build()

    /**
     * <code>string creatorId = 1;</code>
     */
    var creatorId: kotlin.String
      @JvmName("getCreatorId")
      get() = _builder.getCreatorId()
      @JvmName("setCreatorId")
      set(value) {
        _builder.setCreatorId(value)
      }
    /**
     * <code>string creatorId = 1;</code>
     */
    fun clearCreatorId() {
      _builder.clearCreatorId()
    }

    /**
     * <code>string title = 2;</code>
     */
    var title: kotlin.String
      @JvmName("getTitle")
      get() = _builder.getTitle()
      @JvmName("setTitle")
      set(value) {
        _builder.setTitle(value)
      }
    /**
     * <code>string title = 2;</code>
     */
    fun clearTitle() {
      _builder.clearTitle()
    }

    /**
     * <code>string description = 3;</code>
     */
    var description: kotlin.String
      @JvmName("getDescription")
      get() = _builder.getDescription()
      @JvmName("setDescription")
      set(value) {
        _builder.setDescription(value)
      }
    /**
     * <code>string description = 3;</code>
     */
    fun clearDescription() {
      _builder.clearDescription()
    }

    /**
     * <code>.org.voting.pollmanagement.PollStatus status = 4;</code>
     */
     var status: org.voting.pollmanagement.Poll.PollStatus
      @JvmName("getStatus")
      get() = _builder.getStatus()
      @JvmName("setStatus")
      set(value) {
        _builder.setStatus(value)
      }
    /**
     * <code>.org.voting.pollmanagement.PollStatus status = 4;</code>
     */
    fun clearStatus() {
      _builder.clearStatus()
    }

    /**
     * <code>int64 price = 5;</code>
     */
    var price: kotlin.Long
      @JvmName("getPrice")
      get() = _builder.getPrice()
      @JvmName("setPrice")
      set(value) {
        _builder.setPrice(value)
      }
    /**
     * <code>int64 price = 5;</code>
     */
    fun clearPrice() {
      _builder.clearPrice()
    }

    /**
     * <code>int32 maxVoters = 6;</code>
     */
    var maxVoters: kotlin.Int
      @JvmName("getMaxVoters")
      get() = _builder.getMaxVoters()
      @JvmName("setMaxVoters")
      set(value) {
        _builder.setMaxVoters(value)
      }
    /**
     * <code>int32 maxVoters = 6;</code>
     */
    fun clearMaxVoters() {
      _builder.clearMaxVoters()
    }

    /**
     * An uninstantiable, behaviorless type to represent the field in
     * generics.
     */
    @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
    class PreferencesProxy private constructor() : com.google.protobuf.kotlin.DslProxy()
    /**
     * <code>map&lt;string, string&gt; preferences = 7;</code>
     */
     val preferences: com.google.protobuf.kotlin.DslMap<kotlin.String, kotlin.String, PreferencesProxy>
      @kotlin.jvm.JvmSynthetic
      @JvmName("getPreferencesMap")
      get() = com.google.protobuf.kotlin.DslMap(
        _builder.getPreferencesMap()
      )
    /**
     * <code>map&lt;string, string&gt; preferences = 7;</code>
     */
    @JvmName("putPreferences")
    fun com.google.protobuf.kotlin.DslMap<kotlin.String, kotlin.String, PreferencesProxy>
      .put(key: kotlin.String, value: kotlin.String) {
         _builder.putPreferences(key, value)
       }
    /**
     * <code>map&lt;string, string&gt; preferences = 7;</code>
     */
    @kotlin.jvm.JvmSynthetic
    @JvmName("setPreferences")
    @Suppress("NOTHING_TO_INLINE")
    inline operator fun com.google.protobuf.kotlin.DslMap<kotlin.String, kotlin.String, PreferencesProxy>
      .set(key: kotlin.String, value: kotlin.String) {
         put(key, value)
       }
    /**
     * <code>map&lt;string, string&gt; preferences = 7;</code>
     */
    @kotlin.jvm.JvmSynthetic
    @JvmName("removePreferences")
    fun com.google.protobuf.kotlin.DslMap<kotlin.String, kotlin.String, PreferencesProxy>
      .remove(key: kotlin.String) {
         _builder.removePreferences(key)
       }
    /**
     * <code>map&lt;string, string&gt; preferences = 7;</code>
     */
    @kotlin.jvm.JvmSynthetic
    @JvmName("putAllPreferences")
    fun com.google.protobuf.kotlin.DslMap<kotlin.String, kotlin.String, PreferencesProxy>
      .putAll(map: kotlin.collections.Map<kotlin.String, kotlin.String>) {
         _builder.putAllPreferences(map)
       }
    /**
     * <code>map&lt;string, string&gt; preferences = 7;</code>
     */
    @kotlin.jvm.JvmSynthetic
    @JvmName("clearPreferences")
    fun com.google.protobuf.kotlin.DslMap<kotlin.String, kotlin.String, PreferencesProxy>
      .clear() {
         _builder.clearPreferences()
       }
  }
}
@kotlin.jvm.JvmSynthetic
inline fun org.voting.pollmanagement.Poll.PollRequest.copy(block: org.voting.pollmanagement.PollRequestKt.Dsl.() -> kotlin.Unit): org.voting.pollmanagement.Poll.PollRequest =
  org.voting.pollmanagement.PollRequestKt.Dsl._create(this.toBuilder()).apply { block() }._build()

