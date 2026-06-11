import org.gradle.api.Plugin
import org.gradle.api.Project

class TestPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        println("Test Plugin Applied to ${target.name}")
    }
}
