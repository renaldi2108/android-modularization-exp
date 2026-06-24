plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace  = "com.example.core.shared.designsystem"
    compileSdk = 35
    defaultConfig { minSdk = 26 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    buildFeatures { compose = true }
}

composeCompiler {
    if (providers.gradleProperty("composeCompilerReports").orNull == "true") {
        reportsDestination.set(layout.buildDirectory.dir("compose_compiler"))
        metricsDestination.set(layout.buildDirectory.dir("compose_compiler"))
    }
}

dependencies {
    val composeBom = platform(libs.androidx.compose.bom)
    api(composeBom)
    api(libs.androidx.ui)
    api(libs.androidx.ui.graphics)
    api(libs.androidx.ui.tooling.preview)
    api(libs.androidx.material3)
    api(libs.androidx.material.icons.ext)

    implementation(libs.androidx.core.ktx)

    debugImplementation(libs.androidx.ui.tooling)
}
