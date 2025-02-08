package localllm.ooba

object Main {

    val client = NSFJSTICareLoopClient("http://nsf-jst-chatbot.ist.osaka-u.ac.jp:8443")
    val userInfo = mapOf(
        "preferredName" to "User",
        "age" to "30",
        "family" to "Single",
        "aboutYourself" to "Desktop user testing the system"
    )


    fun runJPTest() {
        val deviceId = "JP-1"
        val tester = MemoryTester(client = client, deviceId = deviceId, language = "ja", userInfo = userInfo)
        tester.runTests()
    }

    fun runEnglishTest() {
        val deviceId = "JP-2"
        val tester = MemoryTester(client = client, deviceId = deviceId, language = "en", userInfo = userInfo)
        tester.runTests()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        //runJPTest()
        runEnglishTest()
    }
}
