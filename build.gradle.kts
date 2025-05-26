plugins {
    kotlin("android") version "2.0.21" apply false
    id("com.android.application") version "8.7.3" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
