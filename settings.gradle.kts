pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "JoinSphere"
include(":app")
include(":feature:auth")
include(":feature:discover")
include(":feature:search")
include(":feature:createevent")
include(":feature:eventdetail")
include(":feature:chat")
include(":feature:profile")
include(":feature:myevents")
include(":feature:settings")

include(":core:domain")
include(":core:data")
include(":core:database")
include(":core:network")
include(":core:model")
include(":core:design")
include(":core:navigation")
include(":feature:onboarding")
