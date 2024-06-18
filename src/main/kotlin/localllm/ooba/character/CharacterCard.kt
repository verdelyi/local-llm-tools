package localllm.ooba.character

data class CharacterCard(
    val name: String,
    val your_name: String,
    val context: String,
    val greeting: String,
    val example_dialogue: String
)