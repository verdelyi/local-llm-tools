package localllm.ooba.requests

import kotlinx.serialization.Serializable

@Serializable
data class StartConversationRequest(
    val deviceId: String
)

@Serializable
data class StartConversationResponse(
    val conversationId: String
)

@Serializable
data class EndConversationRequest(
    val deviceId: String,
    val conversationId: String
)

@Serializable
data class ChatRequest(
    val conversationId: String,
    val question: String,
    val timestamp: String,
    val preferredName: String,
    val age: String,
    val family: String,
    val aboutYourself: String,
)

@Serializable
data class ChatResponse(val text: String)
