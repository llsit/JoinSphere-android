package com.llsit.joinsphere

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidFlavorConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.findByType(ApplicationExtension::class.java)?.apply {
                flavorDimensions += "environment"
                productFlavors {
                    create("staging") {
                        dimension = "environment"
                        applicationIdSuffix = ".staging"
                    }
                    create("prod") {
                        dimension = "environment"
                    }
                }
            }
            extensions.findByType(LibraryExtension::class.java)?.apply {
                flavorDimensions += "environment"
                productFlavors {
                    create("staging") {
                        dimension = "environment"
                    }
                    create("prod") {
                        dimension = "environment"
                    }
                }
            }
        }
    }
}
