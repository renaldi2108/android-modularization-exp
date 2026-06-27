import com.example.buildlogic.featureDomainProjectPath
import com.example.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class FeatureDataConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        apply<AndroidLibraryConventionPlugin>()
        apply<AndroidHiltConventionPlugin>()

        dependencies {
            "api"(project(featureDomainProjectPath()))
            "implementation"(project(":core:utils"))
            "implementation"(project(":core:network"))

            "implementation"(libs.findLibrary("kotlinx-coroutines-android").get())
            "implementation"(libs.findLibrary("moshi").get())
            "ksp"(libs.findLibrary("moshi-kotlin-codegen").get())

            "testImplementation"(libs.findLibrary("junit").get())
            "testImplementation"(libs.findLibrary("kotlinx-coroutines-test").get())
        }
    }
}
