package localllm.ooba

import kotlinx.serialization.Serializable

@Serializable
class ChatHistory(
    val internal: List<List<String>>,
    val visible: List<List<String>>
) {
    fun shitfUp(): ChatHistory {
        val newInternal = mutableListOf<List<String>>()
        val newVisible = mutableListOf<List<String>>()

        val internalFlat = internal.flatten().drop(1)
        (0 until internalFlat.size/2).forEach { idx ->
            newInternal.add(mutableListOf(internalFlat[idx * 2], internalFlat[idx * 2 + 1]))
        }

        val visibleFlat = visible.flatten().drop(1)
        (0 until visibleFlat.size/2).forEach { idx ->
            newVisible.add(mutableListOf(visibleFlat[idx * 2], visibleFlat[idx * 2 + 1]))
        }
        return ChatHistory(internal = newInternal, visible = newVisible)
    }

    fun toString(character: String, username: String): String {
        val sb = StringBuilder()
        internal.forEach {
            val ownLine = "[$username]: ${LLMUtil.removeExcessNewlines(it[0])}"
            val aiLine = "[$character]: ${LLMUtil.removeExcessNewlines(it[1])}"
            sb.appendLine(ownLine)//.appendLine()
            sb.appendLine(aiLine)//.appendLine()
        }
        return sb.toString()
    }
}