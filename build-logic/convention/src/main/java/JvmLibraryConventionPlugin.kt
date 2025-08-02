
import id.dev.convention.configureKotlinJvm
import id.dev.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class JvmLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.apply(libs.findPlugin("kotlin.jvm").get().get().pluginId)

            configureKotlinJvm()
        }
    }
}