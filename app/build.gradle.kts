plugins {
    id("com.android.application")
    kotlin("android")
    id("io.github.takahirom.roborazzi")
}

android {
    namespace = "net.hiraok.composesample"
    compileSdk = 34

    defaultConfig {
        applicationId = "net.hiraok.composesample"
        minSdk = 28
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    kotlinOptions {
       jvmTarget = "17"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
}

dependencies {

    implementation(project(":media:movie"))
    implementation(project(":map"))
    implementation(project(":museum"))
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.media3:media3-common:1.1.1")
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.runtime:runtime:1.5.4")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.4")
    implementation("androidx.compose.material:material:1.5.4")
    implementation("androidx.navigation:navigation-compose:2.7.5")
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test.ext:junit:1.1.5")
    testImplementation("androidx.compose.ui:ui-test-junit4-android:1.5.4")
    testImplementation("io.github.takahirom.roborazzi:roborazzi:1.8.0-alpha-4")
    testImplementation("io.github.takahirom.roborazzi:roborazzi-compose:1.8.0-alpha-4")
    testImplementation("io.github.takahirom.roborazzi:roborazzi-junit-rule:1.8.0-alpha-4")
    testImplementation("org.robolectric:robolectric:4.11.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.4")
}