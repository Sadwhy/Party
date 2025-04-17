plugins {
    kotlin("android") version "2.1.0" apply false
    id("com.android.application") version "8.7.3" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
	maven("https://jitpack.io")
    }
}
