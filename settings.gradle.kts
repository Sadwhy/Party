pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("gradle/libs.versions.toml"))
        }
    }
}
rootProject.name = "su-party"
include(":app")
