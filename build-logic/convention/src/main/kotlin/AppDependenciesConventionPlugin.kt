import com.example.app.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class AppDependenciesConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val features = listOf(
            "auth", "products", "users", "posts", "todos",
            "quotes", "carts", "recipes", "comments",
        )

        dependencies {
            features.forEach { feature ->
                add("implementation", project(":feature:$feature:presentation"))
                add("implementation", project(":feature:$feature:data"))
            }

            add("implementation", project(":core:utils"))
            add("implementation", project(":core:network"))
            add("implementation", project(":core:datastore"))
            add("implementation", project(":core:shared:designsystem"))

            add("implementation", libs.findLibrary("hilt-navigation-compose").get())
            add("implementation", libs.findLibrary("androidx-core-ktx").get())
            add("implementation", libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
            add("implementation", libs.findLibrary("androidx-activity-compose").get())
            add("implementation", libs.findLibrary("androidx-navigation-compose").get())
            add("implementation", libs.findLibrary("kotlinx-coroutines-android").get())

            add("debugImplementation", libs.findLibrary("androidx-ui-tooling").get())
            add("debugImplementation", libs.findLibrary("androidx-ui-test-manifest").get())
        }
    }
}
