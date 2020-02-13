package question

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.toml
import com.uchuhimo.konf.toValue
import java.io.File

data class AppConfig(
    val outputs: Outputs
)

data class Outputs(
    val optionA: OptionA? = null
)

data class OptionA(val name: String)


fun loadConfigFromTomlOnly(configFile: File): AppConfig {
    return Config()
        .from.toml.file(configFile)
        .toValue()
}

fun loadConfigFromTomlAndEnv(configFile: File): AppConfig {
    return Config()
        .from.toml.file(configFile)
        .from.env()
        .toValue()
}


