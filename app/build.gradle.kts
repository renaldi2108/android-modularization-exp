plugins {
    id("androidbase.android.application")
    id("androidbase.android.compose")
    id("androidbase.android.hilt")
}

android {
    defaultConfig {
        applicationId = "com.example.app"
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
        buildConfig = true
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

    implementation(libs.hilt.navigation.compose)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.coroutines.android)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
