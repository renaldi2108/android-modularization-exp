plugins {
    id("androidbase.android.library")
}

dependencies {
    api(project(":core:common"))

    api(libs.kotlinx.collections.immutable)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.coroutines.android)
}
