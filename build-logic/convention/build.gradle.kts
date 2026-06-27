import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.example.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "androidbase.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "androidbase.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("jvmLibrary") {
            id = "androidbase.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = "androidbase.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("androidHilt") {
            id = "androidbase.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("featureDomain") {
            id = "androidbase.feature.domain"
            implementationClass = "FeatureDomainConventionPlugin"
        }
        register("featureData") {
            id = "androidbase.feature.data"
            implementationClass = "FeatureDataConventionPlugin"
        }
        register("featurePresentation") {
            id = "androidbase.feature.presentation"
            implementationClass = "FeaturePresentationConventionPlugin"
        }
    }
}
