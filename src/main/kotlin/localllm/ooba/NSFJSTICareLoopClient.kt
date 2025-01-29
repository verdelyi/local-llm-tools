package localllm.ooba


import kotlinx.serialization.json.Json
import localllm.ooba.requests.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class NSFJSTICareLoopClient(val host: String) {
    private val TAG = this::class.simpleName

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .callTimeout(30, TimeUnit.SECONDS)
        .build()

    fun sendChatMessage(
        message: String,
        conversationId: String,
        preferredName: String,
        age: String,
        family: String,
        aboutYourself: String,
    ): String {
        val chatRequest = ChatRequest(
            conversationId = conversationId,
            question = message,
            timestamp = OffsetDateTime.now().format(DateTimeFormatter.ISO_INSTANT),
            preferredName = preferredName,
            age = age,
            family = family,
            aboutYourself = aboutYourself
        )

        val requestBodyJson = Json.encodeToString(chatRequest)
        println("request body: $requestBodyJson")
        val request = Request.Builder()
            .url("$host/chat/generate-response")
            .addHeader("Content-Type", "application/json")
            .post(requestBodyJson.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        val response = httpClient.newCall(request).execute()
        val responseBody = response.body?.string() ?: throw IllegalStateException("Empty response body")
        println("response body: $responseBody")
        val responseObj = Json.decodeFromString<ChatResponse>(responseBody)
        return responseObj.text
    }

    fun startConversation(deviceId: String): String {
        val startRequest = StartConversationRequest(deviceId = deviceId)
        val requestBodyJson = Json.encodeToString(startRequest)

        val request = Request.Builder()
            .url("$host/chat/start-conversation")
            .addHeader("Content-Type", "application/json")
            .post(requestBodyJson.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        val response = httpClient.newCall(request).execute()
        val responseBody =
            response.body?.string() ?: throw IllegalStateException("Empty response body")
        //println("Start conversation response: $responseBody")
        val cid = Json.decodeFromString<StartConversationResponse>(responseBody).conversationId
        println("Started new conversation (cid: $cid)")
        return cid
    }

    fun endConversation(conversationId: String) {
        val endRequest = EndConversationRequest(conversationId)
        val requestBodyJson = Json.encodeToString(endRequest)
        val request = Request.Builder()
            .url("$host/chat/end-conversation")
            .addHeader("Content-Type", "application/json")
            .post(requestBodyJson.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        val response = httpClient.newCall(request).execute()
        if (!response.isSuccessful) {
            println("Failed to end conversation: ${response.code}")
            throw IllegalStateException("Failed to end conversation: ${response.code}")
        }
        println("Successfully ended conversation $conversationId")
    }
}