package localllm.ooba

import kotlinx.serialization.json.Json
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.io.path.writeText

object LLMUtil {
    fun removeExcessNewlines(s: String): String {
        return s.replace("\n\n\n", "\n")
            .replace("\n\n", "\n").trim()
        //.replace("*", "  ")
    }

    fun convertChatHistoryJSON2TXT(infile: String, outfile: String, character: String, username: String) {
        val obj = Json.decodeFromString<ChatHistory>(Path.of(infile).readText())
        val text = obj.toString(character = character, username = username)
        Path.of(outfile).writeText(text)
    }
}