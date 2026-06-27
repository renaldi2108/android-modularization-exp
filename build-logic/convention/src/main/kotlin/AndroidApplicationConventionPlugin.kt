import com.android.build.api.dsl.ApplicationExtension
import com.example.app.buildlogic.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.application")
            apply("org.jetbrains.kotlin.android")
        }

        extensions.configure<ApplicationExtension> {
            configureKotlinAndroid(this)

            defaultConfig {
                applicationId = "com.example.app"
                targetSdk = 35
                versionCode = 1
                versionName = "1.0.0"

                buildConfigField("String", "BASE_URL", "\"https://dummyjson.com/\"")
            }

            buildTypes {
                release {
                    isMinifyEnabled = true
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro",
                    )
                }
                debug {
                    applicationIdSuffix = ".debug"
                    isDebuggable = true
                }
            }

            buildFeatures {
                buildConfig = true
            }
        }
    }
}
