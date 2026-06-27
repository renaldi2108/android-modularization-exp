plugins {
    id("androidbase.android.library")
    id("androidbase.android.hilt")
}

dependencies {
    api(libs.androidx.datastore.preferences)

    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}
