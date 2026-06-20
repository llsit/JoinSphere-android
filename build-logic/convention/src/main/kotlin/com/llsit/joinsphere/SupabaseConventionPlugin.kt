package com.llsit.joinsphere

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class SupabaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                val bom = libs.findLibrary("supabase-bom").get()
                add("implementation", platform(bom))
                add("implementation", libs.findLibrary("supabase-kt").get())
                add("implementation", libs.findLibrary("supabase-auth").get())
                add("implementation", libs.findLibrary("supabase-postgrest").get())
                add("implementation", libs.findLibrary("supabase-storage").get())
                add("implementation", libs.findLibrary("ktor-client-okhttp").get())
            }
        }
    }
}
