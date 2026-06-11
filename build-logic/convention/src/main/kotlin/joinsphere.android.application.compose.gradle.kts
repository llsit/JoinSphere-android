plugins {
    id("joinsphere.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    buildFeatures {
        compose = true
    }
}

dependencies {
    val bom = libs.findLibrary("androidx-compose-bom").get()
    implementation(platform(bom))
    androidTestImplementation(platform(bom))

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
