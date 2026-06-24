plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace  = "com.example.core.shared.ui"
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
    api(project(":core:utils"))
    api(project(":core:shared:designsystem"))

    api(libs.hilt.navigation.compose)
    api(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel)
}
