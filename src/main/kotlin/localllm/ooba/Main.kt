package localllm.ooba

object Main {
    val client = NSFJSTICareLoopClient("http://nsf-jst-chatbot.ist.osaka-u.ac.jp:8443")
    val deviceId = "JP-1000"
    val userInfo = mapOf(
        "preferredName" to "User",
        "age" to "30",
        "family" to "Single",
        "aboutYourself" to "Desktop user testing the system"
    )

    private fun sendMsg(msg: String, conversationId: String) {
        val response = client.sendChatMessage(
            message = msg,
            conversationId = conversationId,
            preferredName = userInfo["preferredName"]!!,
            age = userInfo["age"]!!,
            family = userInfo["family"]!!,
            aboutYourself = userInfo["aboutYourself"]!!
        )
        println("Received response: $response")
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val conv1 = client.startConversation(deviceId)
        sendMsg("こんにちは、私は空手が好きです", conv1)
//        sendMsg("こんにちは、私の息子は35歳です", conv1)
        client.endConversation(conv1)

        val conv2 = client.startConversation(deviceId)
        sendMsg("こんにちは、私は何が好きでしたっけ？", conv2)
//        sendMsg("こんにちは、私の息子は何歳でしたっけ？", conv2)
        client.endConversation(conv2)
    }
}
