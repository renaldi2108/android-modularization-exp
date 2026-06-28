plugins {
    id("androidbase.android.application")
    id("androidbase.android.compose")
    id("androidbase.android.hilt")
    id("androidbase.app.dependencies")
    alias(libs.plugins.baselineprofile)
}

dependencies {
    implementation(libs.androidx.profileinstaller)
    baselineProfile(project(":baselineprofile"))
}
