plugins {
    id("androidbase.jvm.library")
}

dependencies {
    api(libs.javax.inject)
    api(libs.kotlinx.coroutines.core)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}
