plugins {
    id("joinsphere.android.application.compose")
    id("joinsphere.supabase")
    id("joinsphere.koin")
    id("joinsphere.navigation")
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.llsit.joinsphere"

    defaultConfig {
        applicationId = "com.llsit.joinsphere"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    productFlavors {
        getByName("staging") {
            buildConfigField("String", "BASE_URL", "\"https://api.staging.example.com/\"")
        }
        getByName("prod") {
            buildConfigField("String", "BASE_URL", "\"https://api.example.com/\"")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    // Features
    implementation(project(":feature:auth"))
    implementation(project(":feature:onboarding"))
    implementation(project(":feature:discover"))
    implementation(project(":feature:search"))
    implementation(project(":feature:createevent"))
    implementation(project(":feature:eventdetail"))
    implementation(project(":feature:chat"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:myevents"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:notifications"))

    // Core
    implementation(project(":core:data"))
    implementation(project(":core:design"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))

    implementation(project(":core:database"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Navigation Compose


    // Ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.kotlinx.serialization.json)

    // Coil
    implementation(libs.coil.compose)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Location & Maps
    implementation(libs.play.services.location)
    implementation(libs.maps.compose)

    // Accompanist
    implementation(libs.accompanist.permissions)

    // Timber
    implementation(libs.timber)

    // Datetime
    implementation(libs.kotlinx.datetime)

    // Material Icons Extended
    implementation(libs.material.icons.extended)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
