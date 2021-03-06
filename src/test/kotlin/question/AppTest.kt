package question

import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import java.io.File
import java.util.Collections

class AppTest {

    @Test
    fun `config from toml only should produce x for optionA name`() {

        // environment variables are set but should be ignored for this test
        setEnv(emptyMap())
        setEnv(mapOf("OUTPUTS_OPTIONA_NAME" to "y"))

        val tomlSampleFile = File(javaClass.getResource("/config.toml").toURI())
        val config = loadConfigFromTomlOnly(tomlSampleFile)

        val optionA = config.outputs.optionA
        optionA?.name.shouldBe("x")
    }

    @Test
    fun `config from toml and env should produce y for optionA name`() {

        // attempt to override the outputs.optionA.name property via env variable.
        // aside from the error being thrown when run, i'm not sure this is the right property name convention.
        setEnv(emptyMap())
        setEnv(mapOf("OUTPUTS_OPTIONA_NAME" to "y"))

        val tomlSampleFile = File(javaClass.getResource("/config.toml").toURI())
        val config = loadConfigFromTomlAndEnv(tomlSampleFile)

        val optionA = config.outputs.optionA
        optionA?.name.shouldBe("y")
    }


    // non-typical code just for this test - used to override the system environment variables in the JVM process
    @Suppress("UNCHECKED_CAST")
    @Throws(Exception::class)
    private fun setEnv(newenv: Map<String, String>) {
        try {
            val processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment")
            val theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment")
            theEnvironmentField.isAccessible = true
            val env = theEnvironmentField.get(null) as MutableMap<String, String>
            env.putAll(newenv)
            val theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment")
            theCaseInsensitiveEnvironmentField.isAccessible = true
            val cienv = theCaseInsensitiveEnvironmentField.get(null) as MutableMap<String, String>
            cienv.putAll(newenv)
        } catch (e: NoSuchFieldException) {
            val classes = Collections::class.java.getDeclaredClasses()
            val env = System.getenv()
            for (cl in classes) {
                if ("java.util.Collections\$UnmodifiableMap" == cl.getName()) {
                    val field = cl.getDeclaredField("m")
                    field.setAccessible(true)
                    val obj = field.get(env)
                    val map = obj as MutableMap<String, String>
                    map.clear()
                    map.putAll(newenv)
                }
            }
        }
    }
}
