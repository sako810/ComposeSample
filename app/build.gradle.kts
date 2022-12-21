plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "net.hiraok.composesample"
    compileSdk = 33

    defaultConfig {
        applicationId = "net.hiraok.composesample"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        compose = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(project(":media:movie"))
    implementation(project(":map"))
    implementation(project(":museum"))
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.media3:media3-common:1.0.0-beta03")
    implementation("androidx.compose.ui:ui:1.4.0-alpha02")
    implementation("androidx.compose.runtime:runtime:1.4.0-alpha02")
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.0-alpha02")
    implementation("androidx.compose.material:material:1.3.1")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    testImplementation("junit:junit:4.12")
}