plugins {
    id("joinsphere.android.library")
    id("joinsphere.koin")
    id("joinsphere.supabase")
}

android {
    namespace = "com.llsit.joinsphere.core.data"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:database"))
    implementation(project(":core:network"))
    implementation(project(":core:model"))

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.timber)
}
