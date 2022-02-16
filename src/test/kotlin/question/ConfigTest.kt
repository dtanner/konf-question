package question

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.hocon
import com.uchuhimo.konf.toValue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ConfigTest {

    data class ServerConfig(val host: String)

    @Test
    fun `foo hocon`() {
        val config = Config()
            .from.hocon.resource("config1.conf")
            .from.hocon.resource("config2-missing-host.conf", optional = true)

        val serverConfig = config.at("server").toValue<ServerConfig>()
        serverConfig.host shouldBe "0.0.0.0"

        /*
        The following line throws:

        item root is unset
        com.uchuhimo.konf.UnsetValueException: item root is unset
        at app//com.uchuhimo.konf.BaseConfig.getOrNull(BaseConfig.kt:187)
        at app//com.uchuhimo.konf.BaseConfig.getOrNull$default(BaseConfig.kt:179)
        at app//com.uchuhimo.konf.BaseConfig.get(BaseConfig.kt:159)
        at app//com.uchuhimo.konf.BaseConfig$property$1.getValue(BaseConfig.kt:527)
         */
        val host = config.at("server.host").toValue<String>()
        host shouldBe "0.0.0.0"
    }

}
