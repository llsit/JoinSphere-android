plugins {
    id("joinsphere.android.library.compose")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.llsit.joinsphere.core.navigation"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:domain"))
    api(libs.androidx.navigation3.runtime)
    api(libs.androidx.navigation3.ui)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.material.icons.extended)
}