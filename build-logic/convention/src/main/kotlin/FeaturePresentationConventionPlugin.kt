import com.example.app.buildlogic.featureDomainProjectPath
import com.example.app.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class FeaturePresentationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        apply<AndroidLibraryConventionPlugin>()
        apply<AndroidComposeConventionPlugin>()
        apply<AndroidHiltConventionPlugin>()

        dependencies {
            "api"(project(featureDomainProjectPath()))
            "implementation"(project(":core:utils"))
            "implementation"(project(":core:shared:ui"))

            "implementation"(libs.findLibrary("hilt-navigation-compose").get())
            "implementation"(libs.findLibrary("androidx-navigation-compose").get())
            "implementation"(libs.findLibrary("androidx-lifecycle-viewmodel").get())
            "implementation"(libs.findLibrary("androidx-core-ktx").get())
            "implementation"(libs.findLibrary("kotlinx-coroutines-android").get())

            "debugImplementation"(libs.findLibrary("androidx-ui-tooling").get())
            "debugImplementation"(libs.findLibrary("androidx-ui-test-manifest").get())

            "testImplementation"(libs.findLibrary("junit").get())
            "testImplementation"(libs.findLibrary("kotlinx-coroutines-test").get())
        }
    }
}
