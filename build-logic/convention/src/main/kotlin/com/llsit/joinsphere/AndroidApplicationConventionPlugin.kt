package com.llsit.joinsphere

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins.apply("com.android.application")
            plugins.apply("joinsphere.android.flavor")

            extensions.configure<ApplicationExtension> {
                compileSdk = 36
                defaultConfig {
                    minSdk = 28
                    targetSdk = 36
                }
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }
            }
        }
    }
}
