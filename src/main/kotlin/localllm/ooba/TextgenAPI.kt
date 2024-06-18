package localllm.ooba

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import localllm.ooba.requests.*
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

/**
 * TextGen API docs are at http://127.0.0.1:5000/docs#/
 */
class TextgenAPI(val host: String = "http://localhost:5000") {
    var character: String = "DUMMY"
    var username: String = "DUMMY"

    private fun sendGETRequest(uri: URI): HttpResponse<String> {
        println("Sending HTTP GET request: $uri")
        val httpReq = HttpRequest.newBuilder().uri(uri).GET().build()
        val client: HttpClient = HttpClient.newBuilder().build()
        val response: HttpResponse<String> = client.send(httpReq, HttpResponse.BodyHandlers.ofString())
        println("Status: ${response.statusCode()}, Response Body: >>>${response.body()}<<<")
        return response
    }

    private fun sendPOSTRequest(uri: URI, requestBodyJson: String): HttpResponse<String> {
        println("Sending HTTP POST request: $requestBodyJson")
        val httpReq = HttpRequest.newBuilder()
            .uri(uri)
            .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
            .build()
        val client: HttpClient = HttpClient.newBuilder().build()
        val response: HttpResponse<String> = client.send(httpReq, HttpResponse.BodyHandlers.ofString())
        println("Status: ${response.statusCode()}, Response Body: >>>${response.body()}<<<")
        return response
    }

    private val json = Json { encodeDefaults = true }

    fun loadModel(modelName: String, instructionTemplate: String) {
        println("Loading model $modelName...")
        val requestBody = ModelRequest(
            model_name = modelName,
            args = ModelLoadArgs(),
            settings = ModelSettings(instruction_template = instructionTemplate)
        )
        sendPOSTRequest(uri = URI("$host/v1/internal/model/load"), requestBodyJson = json.encodeToString(requestBody))
    }

    fun printModelInfo() {
        val response = sendGETRequest(uri = URI("$host/v1/internal/model/info"))
        val responseJSONElement = json.parseToJsonElement(response.body())
        val modelName = responseJSONElement.jsonObject["model_name"]!!
        val loras = responseJSONElement.jsonObject["lora_names"]!!.jsonArray
        println("[Model info] Name $modelName, Lora(s): $loras")
    }

    fun sendChatMessage(history: List<MsgItem>): MsgItem {
//        println(">>> History:")
//        println(historyToUse.toString(character, username))
//        println(">>> Input (character: ${username}): $userInput")

        // See fastAPI docs http://localhost:5000/docs esp. chatcompletionrequest
        val genreq = TextGenRequestOpenAIAPI(
            messages = history,
            mode = "chat-instruct", // Valid options: 'chat', 'chat-instruct', 'instruct'
            character = character,
            chat_instruct_command = "Continue the chat dialogue below, talking like a cat. " +
                    "Write a single reply for the character \"<|character|>\".\n\n<|prompt|>",
            name1 = username,
            name2 = character,
            max_tokens = 100,
            min_p = 0.05,
            temperature = 2.2,
            mirostat_mode = 0,
            mirostat_tau = 5.0,
            mirostat_eta = 0.1,
        )
        val response = sendPOSTRequest(uri = URI("$host/v1/chat/completions"), requestBodyJson = json.encodeToString(genreq))
        val modelResponse = json.decodeFromString<TextGenModelResponse>(response.body().toString())
        check(modelResponse.choices.size == 1)
        return modelResponse.choices[0].message
    }
}