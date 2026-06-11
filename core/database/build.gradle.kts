plugins {
    id("joinsphere.android.library")
    id("joinsphere.koin")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.llsit.joinsphere.core.database"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
}
