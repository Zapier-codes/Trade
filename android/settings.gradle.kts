pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Trade"

include(":app")
include(":core-theme")
include(":core-ui")
include(":core-navigation")

project(":app").projectDir = file("app")
project(":core-theme").projectDir = file("core-theme")
project(":core-ui").projectDir = file("core-ui")
project(":core-navigation").projectDir = file("core-navigation")
