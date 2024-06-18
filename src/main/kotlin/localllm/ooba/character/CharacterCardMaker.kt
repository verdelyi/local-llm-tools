package localllm.ooba.character

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import localllm.ooba.character.CharacterCard
import java.nio.file.Files
import java.nio.file.Path

class CharacterCardMaker {
    private val yourName = "User"
    private val charName = "Kate"

    private val workdir = Path.of("D:/AI/LLM character cards")

    val charAndUserPersonas = Files.readString(workdir.resolve("persona.txt"))
    val scenario = Files.readString(workdir.resolve("scenario.txt"))
    val greeting = "" //Files.readString(baseDir.resolve("greeting-sleep.txt"))
    val exampleDialogue = ""

    /** [outfile] should be a .yaml file */
    fun run(outfile: Path) {
        val yamlFactory = YAMLFactory()
            .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
            .disable(YAMLGenerator.Feature.SPLIT_LINES)
            .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
        val mapper = ObjectMapper(yamlFactory)
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        val yamlString = mapper.writeValueAsString(
            CharacterCard(
                name = charName,
                your_name = yourName,
                context = "$charAndUserPersonas\n\nScenario: $scenario\n\n",
                greeting = greeting,
                example_dialogue = exampleDialogue
            )
        )
        Files.writeString(outfile, yamlString)
    }

}