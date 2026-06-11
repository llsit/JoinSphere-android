plugins {
    id("joinsphere.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.llsit.joinsphere.core.model"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
}
