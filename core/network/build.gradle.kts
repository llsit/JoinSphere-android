plugins {
    id("joinsphere.android.library")
    id("joinsphere.koin")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.llsit.joinsphere.core.network"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.kotlinx.serialization.json)
}
