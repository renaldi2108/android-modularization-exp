plugins {
    id("androidbase.android.library")
    id("androidbase.android.compose")
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
