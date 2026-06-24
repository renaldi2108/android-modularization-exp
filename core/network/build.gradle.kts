plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace  = "com.example.core.network"
    compileSdk = 35
    defaultConfig { minSdk = 26 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    api(project(":core:utils"))

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    api(libs.retrofit)
    api(libs.retrofit.moshi)
    api(libs.okhttp)
    api(libs.okhttp.logging)
    api(libs.moshi)
    ksp(libs.moshi.kotlin.codegen)

    testImplementation(libs.junit)
}
