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
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
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
        sourceCompatibility = JavaVersion.VERSION_19
        targetCompatibility = JavaVersion.VERSION_19
    }

    kotlinOptions {
        jvmTarget = "19"
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.fragment.ktx)
    implementation(libs.constraintlayout)

    implementation(platform(libs.platforms.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.activity.compose)

    implementation(libs.serialization.json)

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.runtime.ktx)

    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)

    implementation(libs.okhttp)
    implementation(platform(libs.platforms.retrofit.bom))
    implementation(libs.retrofit)
    implementation(libs.converter.serialization)

    implementation(platform(libs.platforms.coil.bom))
    implementation(libs.coil)
    implementation(libs.coil.okhttp)

    implementation(libs.readmore.view)
    implementation(libs.subsampling.view)
}