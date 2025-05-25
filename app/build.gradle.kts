plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("plugin.serialization")
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

    lint {
        disable += "NullSafeMutableLiveData"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
    // Core AndroidX Libraries
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.13.0-alpha12")
    implementation("androidx.fragment:fragment-ktx:1.8.6")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")

    // Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")

    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    // Lifecycle Components
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.0")

    // Navigation Components (BOM)
    implementation(platform("androidx.navigation:navigation-bom:2.8.9"))
    implementation("androidx.navigation:navigation-fragment-ktx")
    implementation("androidx.navigation:navigation-ui-ktx")

    // Networking Libraries
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation(platform("com.squareup.retrofit2:retrofit-bom:2.11.0"))
    implementation("com.squareup.retrofit2:retrofit")
    implementation("com.squareup.retrofit2:converter-kotlinx-serialization")

    // Coil (BOM)
    implementation(platform("io.coil-kt.coil3:coil-bom:3.2.0"))
    implementation("io.coil-kt.coil3:coil")
    implementation("io.coil-kt.coil3:coil-network-okhttp")

    // UI Utilities
    implementation("com.webtoonscorp.android:readmore-view:1.4.0")
    implementation("com.davemorrissey.labs:subsampling-scale-image-view-androidx:3.10.0")
}