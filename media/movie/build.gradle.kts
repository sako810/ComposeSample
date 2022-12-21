plugins {
    id("com.android.library")
    kotlin("android")
    id("maven-publish")
}

android {
    namespace = "net.hiraok.movie"
    compileSdk = 33

    defaultConfig {
        minSdk = 26
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
}

afterEvaluate {
    val group: String by project
    val pomArtifactId: String by project
    val versionName: String by project

    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = group
                artifactId = pomArtifactId
                version = versionName
                from(components["release"])
            }
            create<MavenPublication>("debug") {
                groupId = group
                artifactId = "$pomArtifactId-debug"
                version = versionName
                from(components["debug"])
            }
        }
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/hiraok/ComposeSample")
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.7.0")
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.compose.ui:ui:1.4.0-alpha02")
    implementation("androidx.compose.runtime:runtime:1.4.0-alpha02")
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.0-alpha02")
    implementation("androidx.compose.material:material:1.3.1")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    // media3
    implementation("androidx.media3:media3-ui:1.0.0-beta03")
    implementation("androidx.media3:media3-exoplayer:1.0.0-beta03")
    implementation("androidx.media3:media3-session:1.0.0-beta03")
    implementation("androidx.media3:media3-extractor:1.0.0-beta03")
    implementation("androidx.media3:media3-cast:1.0.0-beta03")
    implementation("androidx.media3:media3-decoder:1.0.0-beta03")
    implementation("androidx.media3:media3-common:1.0.0-beta03")
    implementation("androidx.media3:media3-transformer:1.0.0-beta03")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("io.coil-kt:coil-compose:2.2.2")

    implementation("androidx.media3:media3-exoplayer-hls:1.0.0-beta03")
    implementation("androidx.media3:media3-exoplayer-dash:1.0.0-beta03")
    implementation("androidx.media3:media3-exoplayer-rtsp:1.0.0-beta03")
    implementation("androidx.media3:media3-exoplayer-smoothstreaming:1.0.0-beta03")
    implementation("androidx.media3:media3-test-utils-robolectric:1.0.0-beta03")
    testImplementation("junit:junit:4.12")
}