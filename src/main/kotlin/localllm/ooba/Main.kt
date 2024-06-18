package localllm.ooba

import localllm.ooba.requests.MsgItem
import java.io.PrintStream
import java.nio.file.Files
import java.nio.file.Path

object Main {

    private fun runSinglePrompt(initialHistory: List<MsgItem>, userMessage: String, api: TextgenAPI, ps: PrintStream) {
        val history = initialHistory.toMutableList()
        history.add(MsgItem(role = "user", content = userMessage))
        val r1 = api.sendChatMessage(history)
        history.add(r1)
        ps.println("--------------------------------------")
        ps.println(history.joinToString("\n") { "[${it.role}] ${it.content}" })
    }

    private fun runWith(modelName: String, instructionTemplate: String) {
        val thePS1 = System.out
        //val thePS2 = PrintStream(Files.newOutputStream(Path.of("C:\\Users\\owner\\Desktop\\llm-test-out").resolve("$modelName.txt")))
        thePS1.use { ps ->
            val api = TextgenAPI()
            ps.println("================ Testing LLM: $modelName ================")
            //api.loadModel(modelName = modelName, instructionTemplate = instructionTemplate)
            api.printModelInfo()

            val history = mutableListOf(
                MsgItem(role = "assistant", content = "Hello, how can I help today?")
            )
//        repeat(3) {
//            println("=========================================================================")
            // Impersonate elderly
            /*api.username = "GeorgeBot"
            api.character = "Elderly"
            val lastAIMessage = api.history.internal.last()[1]
            val h = api.sendChatMessage(userInput = lastAIMessage, chatHistoryOverride = api.history.shitfUp())
            val elderlyMessage = h.internal.last()[1]*/
//            println(">>> Model response (character: ${api.character}): $elderlyMessage")

//            println("=========================================================================")
            // normal roles
            api.username = "Jack"
            api.character = "Assistant"

            listOf("Who are you?")
                .map { runSinglePrompt(initialHistory = history, userMessage = it, api = api, ps = ps) }
            //println(">>> Model response (character: ${api.character}): $r1")
//        }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        runWith(modelName = "bartowski_Hermes-2-Theta-Llama-3-8B-exl2_6_5", instructionTemplate = "ChatML")
//        runWith(modelName = "mythologic-l2-13b.Q6_K.gguf", instructionTemplate = "Alpaca")
//        runWith(modelName = "mythomax-l2-13b.Q6_K.gguf", instructionTemplate = "Alpaca")
//        runWith(modelName = "pygmalion-2-13b.Q6_K.gguf", instructionTemplate = "Metharme")
//        runWith(modelName = "mythalion-13b.Q6_K.gguf", instructionTemplate = "Metharme")
//        runWith(modelName = "wizardlm-13b-v1.2.Q6_K.gguf", instructionTemplate = "Vicuna-v1.1")
//        runWith(modelName = "vicuna-13b-v1.5.Q6_K.gguf", instructionTemplate = "Vicuna-v1.1")
//        runWith(modelName = "nous-hermes-llama2-13b.Q6_K.gguf", instructionTemplate = "Alpaca")
//        runWith(modelName = "openhermes-2.5-mistral-7b.Q6_K.gguf", instructionTemplate = "ChatML")
//        runWith(modelName = "LLaMA2-13B-Tiefighter.Q6_K.gguf", instructionTemplate = "Alpaca")
//        runWith(modelName = "LLaMA2-13B-Psyfighter2.Q6_K.gguf", instructionTemplate = "Alpaca")
    }
}
