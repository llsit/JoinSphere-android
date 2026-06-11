plugins {
    `kotlin-dsl`
}

group = "com.llsit.joinsphere.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "joinsphere.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "joinsphere.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidApplication") {
            id = "joinsphere.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "joinsphere.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("koin") {
            id = "joinsphere.koin"
            implementationClass = "KoinConventionPlugin"
        }
    }
}
