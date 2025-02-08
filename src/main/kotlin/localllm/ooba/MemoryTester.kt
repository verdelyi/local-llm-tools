package localllm.ooba

class MemoryTester(
    private val client: NSFJSTICareLoopClient,
    private val deviceId: String,
    private val language: String,
    private val userInfo: Map<String, String>
) {
    private val memoryTests = mapOf(
        "ja" to listOf(
            // Family
            TestCase(
                initialStatement = "孫の健一は大学で工学を勉強しています",
                query = "孫の勉強について何を話しましたか？",
                expectedKeywords = listOf("孫", "工学", "大学")
            ),
            TestCase(
                initialStatement = "娘の美咲が先週末に遊びに来てくれました",
                query = "娘について何を話しましたか？",
                expectedKeywords = listOf("娘", "美咲", "先週末")
            ),
            // Health
            TestCase(
                initialStatement = "関節炎のため、毎朝公園を散歩しています",
                query = "朝の散歩について何を話しましたか？",
                expectedKeywords = listOf("散歩", "公園", "朝", "関節炎")
            ),
            TestCase(
                initialStatement = "医者に血圧に気をつけるように言われました",
                query = "健康について何か言われましたか？",
                expectedKeywords = listOf("医者", "血圧")
            ),
            // Hobbies
            TestCase(
                initialStatement = "コミュニティセンターの編み物グループに参加しました",
                query = "どんな活動に参加したと言いましたか？",
                expectedKeywords = listOf("コミュニティセンター", "編み物")
            ),
            TestCase(
                initialStatement = "今年は庭でトマトを育てています",
                query = "庭のことについて何か話しましたか？",
                expectedKeywords = listOf("庭", "トマト")
            ),
            // Daily Life
            TestCase(
                initialStatement = "毎朝コーヒーを飲みながらクロスワードパズルを解いています",
                query = "朝の習慣について何を話しましたか？",
                expectedKeywords = listOf("コーヒー", "クロスワード", "朝")
            ),
            TestCase(
                initialStatement = "隣人の佐藤さんがタブレットの使い方を教えてくれました",
                query = "タブレットの件で誰が手伝ってくれましたか？",
                expectedKeywords = listOf("佐藤", "タブレット", "隣人")
            )
        ),
        "en" to listOf(
            // Family
            TestCase(
                initialStatement = "My grandson Tom is studying engineering at college",
                query = "What did I tell you about my grandson's studies?",
                expectedKeywords = listOf("grandson", "engineering", "college")
            ),
            TestCase(
                initialStatement = "My daughter Sarah visited me last weekend",
                query = "When did my daughter visit?",
                expectedKeywords = listOf("daughter", "Sarah", "weekend")
            ),
            // Health
            TestCase(
                initialStatement = "I've been walking in the park every morning for my arthritis",
                query = "What did I say about my morning walks?",
                expectedKeywords = listOf("walking", "park", "morning", "arthritis")
            ),
            TestCase(
                initialStatement = "The doctor says I need to watch my blood pressure",
                query = "What did the doctor tell me about my health?",
                expectedKeywords = listOf("doctor", "blood pressure")
            ),
            // Hobbies
            TestCase(
                initialStatement = "I joined a knitting group at the community center",
                query = "What group did I join?",
                expectedKeywords = listOf("knitting", "community center")
            ),
            TestCase(
                initialStatement = "I'm growing tomatoes in my garden this year",
                query = "Did I mention anything about my garden?",
                expectedKeywords = listOf("garden", "tomatoes", "growing")
            ),
            // Daily Life
            TestCase(
                initialStatement = "I do crossword puzzles every morning with coffee",
                query = "What's my morning routine?",
                expectedKeywords = listOf("crossword", "coffee", "morning")
            ),
            TestCase(
                initialStatement = "My neighbor Carol helped me with my tablet",
                query = "Who helped me with my tablet?",
                expectedKeywords = listOf("Carol", "tablet", "neighbor")
            )
        )
    )

    data class TestCase(
        val initialStatement: String,
        val query: String,
        val expectedKeywords: List<String>
    )

    fun runTests() {
        println("\n====== Starting Memory Tests for $language ======")

        val tests = memoryTests[language] ?: throw IllegalArgumentException("Unsupported language: $language")
        tests.forEach { testCase ->
            runSingleTest(testCase)
        }
    }

    private fun runSingleTest(testCase: TestCase) {
        println("\nTest Case:")
        println("Initial statement: ${testCase.initialStatement}")
        println("Query: ${testCase.query}")
        println("Expected keywords: ${testCase.expectedKeywords.joinToString(", ")}")
        println("-------------------------------------------------------------------------------")

        try {
            // First conversation - store the memory
            val conv1 = client.startConversation(deviceId)
            val response1 = sendMessage(testCase.initialStatement, conv1)
            println("Initial response: $response1")
            client.endConversation(deviceId, conv1)

            // Second conversation - test memory retrieval
            val conv2 = client.startConversation(deviceId)
            val response2 = sendMessage(testCase.query, conv2)
            println("Memory retrieval response: $response2")

            // Verify response contains expected keywords
            val foundKeywords = testCase.expectedKeywords.filter {
                response2.contains(it, ignoreCase = true)
            }

            if (foundKeywords.isNotEmpty()) {
                println("✓ Found keywords: ${foundKeywords.joinToString(", ")}")
            } else {
                println("⚠ No expected keywords found in response")
            }

            client.endConversation(deviceId, conv2)
        } catch (e: Exception) {
            println("❌ Error during test: ${e.message}")
        }
        println("-------------------------------------------------------------------------------")
    }

    private fun sendMessage(msg: String, conversationId: String): String {
        return client.sendChatMessage(
            message = msg,
            conversationId = conversationId,
            preferredName = userInfo["preferredName"]!!,
            age = userInfo["age"]!!,
            family = userInfo["family"]!!,
            aboutYourself = userInfo["aboutYourself"]!!
        )
    }
}