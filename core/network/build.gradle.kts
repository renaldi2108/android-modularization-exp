plugins {
    id("androidbase.android.library")
    id("androidbase.android.hilt")
}

dependencies {
    api(project(":core:utils"))

    api(libs.retrofit)
    api(libs.retrofit.moshi)
    api(libs.okhttp)
    api(libs.okhttp.logging)
    api(libs.moshi)
    ksp(libs.moshi.kotlin.codegen)

    testImplementation(libs.junit)
}
