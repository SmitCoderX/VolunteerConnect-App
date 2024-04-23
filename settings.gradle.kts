pluginManagement {
    repositories {
        google()
        maven (  url ="https://jitpack.io" )
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven (  url ="https://jitpack.io" )
        maven( url = "https://maven.mappls.com/repository/mappls/")
    }
}

rootProject.name = "VolunteerConnect"
include(":app")
