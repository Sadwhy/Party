plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "io.sadwhy.party"
    compileSdk = 35

    defaultConfig {
        applicationId = "io.sadwhy.party"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Core
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.13.0-alpha12")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // UI
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.9")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.9")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")

    // Image loader
    implementation("io.coil-kt.coil3:coil:3.1.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.1.0")
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    implementation("io.getstream:photoview:1.0.3")
}
