plugins {
    kotlin("android") version "1.9.23" apply false
    id("com.android.application") version "8.3.1" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
