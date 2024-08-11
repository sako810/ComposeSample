plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

group = "net.hiraok.compose_samples.build_logic"

dependencies {
    implementation(libs.android.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.compose.gradle.plugin)
    implementation(libs.ksp.gradle.plugin)
    implementation(libs.hilt.android.gradle.plugin)
    implementation(libs.roborazzi.gradle.plugin)
}

gradlePlugin {
    plugins.create("androidApplicationPlugin") {
        id = "net.hiraok.application"
        implementationClass = "net.hiraok.compose_sample.primitive.AndroidApplicationPlugin"
    }
    plugins.create("androidLibraryPlugin") {
        id = "net.hiraok.library"
        implementationClass = "net.hiraok.compose_sample.primitive.AndroidLibraryPlugin"
    }
    plugins.create("androidComposePlugin'") {
        id = "net.hiraok.compose"
        implementationClass = "net.hiraok.compose_sample.primitive.AndroidComposePlugin"
    }
    plugins.create("androidTestPlugin") {
        id = "net.hiraok.test"
        implementationClass = "net.hiraok.compose_sample.primitive.AndroidTestPlugin"
    }
    plugins.create("androidFeaturePlugin") {
        id = "net.hiraok.feature"
        implementationClass = "net.hiraok.compose_sample.convention.AndroidFeaturePlugin"
    }
}