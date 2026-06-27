import com.example.app.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class FeatureDomainConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        apply<JvmLibraryConventionPlugin>()

        dependencies {
            "api"(project(":core:common"))
            "implementation"(libs.findLibrary("kotlinx-coroutines-core").get())

            "testImplementation"(libs.findLibrary("junit").get())
            "testImplementation"(libs.findLibrary("kotlinx-coroutines-test").get())
        }
    }
}
