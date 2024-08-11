plugins {
    id("net.hiraok.application")
    id("net.hiraok.test")
}

android {
    namespace = "net.hiraok.compose_sample"
    compileSdk = 34
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":feature:top"))
}