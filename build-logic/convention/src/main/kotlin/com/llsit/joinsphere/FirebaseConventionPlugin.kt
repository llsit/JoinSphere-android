package com.llsit.joinsphere

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class FirebaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            
            plugins.apply("com.google.gms.google-services")
//            plugins.apply("com.google.firebase.crashlytics")

            dependencies {
                val bom = libs.findLibrary("firebase-bom").get()
                add("implementation", platform(bom))
                add("implementation", libs.findLibrary("firebase-auth").get())
                add("implementation", libs.findLibrary("firebase-analytics").get())
//                add("implementation", libs.findLibrary("firebase-crashlytics").get())
            }
        }
    }
}
