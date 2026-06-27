import com.android.build.api.dsl.CommonExtension
import com.example.app.buildlogic.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

        val extension = extensions.getByName("android") as CommonExtension<*, *, *, *, *, *>
        configureAndroidCompose(extension)
    }
}
