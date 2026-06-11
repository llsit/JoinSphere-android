plugins {
    id("joinsphere.android.library.compose")
    id("joinsphere.koin")
}

android {
    namespace = "com.llsit.joinsphere.feature.search"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:design"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
}
