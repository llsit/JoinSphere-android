plugins {
    id("joinsphere.android.library")
    id("joinsphere.koin")
}

android {
    namespace = "com.llsit.joinsphere.core.domain"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:model"))
}
