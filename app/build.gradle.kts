plugins {
    id("net.hiraok.application")
}

android {
    namespace = "net.hiraok.compose_sample"
    compileSdk = 34
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":feature:top"))
}