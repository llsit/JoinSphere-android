plugins {
    `kotlin-dsl`
}

group = "com.llsit.joinsphere.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "joinsphere.android.library"
            implementationClass = "com.llsit.joinsphere.AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "joinsphere.android.library.compose"
            implementationClass = "com.llsit.joinsphere.AndroidLibraryComposeConventionPlugin"
        }
        register("androidApplication") {
            id = "joinsphere.android.application"
            implementationClass = "com.llsit.joinsphere.AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "joinsphere.android.application.compose"
            implementationClass = "com.llsit.joinsphere.AndroidApplicationComposeConventionPlugin"
        }
        register("koin") {
            id = "joinsphere.koin"
            implementationClass = "com.llsit.joinsphere.KoinConventionPlugin"
        }
        register("navigation") {
            id = "joinsphere.navigation"
            implementationClass = "com.llsit.joinsphere.NavigationConventionPlugin"
        }
        register("androidFlavor") {
            id = "joinsphere.android.flavor"
            implementationClass = "com.llsit.joinsphere.AndroidFlavorConventionPlugin"
        }
        register("supabase") {
            id = "joinsphere.supabase"
            implementationClass = "com.llsit.joinsphere.SupabaseConventionPlugin"
        }
    }
}
