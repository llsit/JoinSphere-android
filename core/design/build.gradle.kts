plugins {
    id("joinsphere.android.library.compose")
}

android {
    namespace = "com.llsit.joinsphere.core.design"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.material.icons.extended)
    implementation(libs.coil.compose)
}
