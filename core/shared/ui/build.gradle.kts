plugins {
    id("androidbase.android.library")
    id("androidbase.android.compose")
}

dependencies {
    api(project(":core:utils"))
    api(project(":core:shared:designsystem"))

    api(libs.hilt.navigation.compose)
    api(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel)
}
