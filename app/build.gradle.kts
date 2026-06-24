plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        buildConfigField("String", "BASE_URL", "\"https://dummyjson.com/\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

composeCompiler {
    if (providers.gradleProperty("composeCompilerReports").orNull == "true") {
        reportsDestination.set(layout.buildDirectory.dir("compose_compiler"))
        metricsDestination.set(layout.buildDirectory.dir("compose_compiler"))
    }
}

dependencies {
    implementation(project(":core:utils"))
    implementation(project(":core:network"))
    implementation(project(":core:datastore"))

    implementation(project(":feature:auth:presentation"))
    implementation(project(":feature:auth:data"))
    implementation(project(":feature:products:presentation"))
    implementation(project(":feature:products:data"))
    implementation(project(":feature:users:presentation"))
    implementation(project(":feature:users:data"))
    implementation(project(":feature:posts:presentation"))
    implementation(project(":feature:posts:data"))
    implementation(project(":feature:todos:presentation"))
    implementation(project(":feature:todos:data"))
    implementation(project(":feature:quotes:presentation"))
    implementation(project(":feature:quotes:data"))
    implementation(project(":feature:carts:presentation"))
    implementation(project(":feature:carts:data"))
    implementation(project(":feature:recipes:presentation"))
    implementation(project(":feature:recipes:data"))
    implementation(project(":feature:comments:presentation"))
    implementation(project(":feature:comments:data"))

    implementation(project(":core:shared:designsystem"))

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.coroutines.android)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
