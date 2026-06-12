plugins {
    id("joinsphere.android.library.compose")
    id("joinsphere.koin")
    id("joinsphere.navigation")
}

android {
    namespace = "com.llsit.joinsphere.feature.auth"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:design"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.material.icons.extended)
    implementation(libs.coil.compose)
}
