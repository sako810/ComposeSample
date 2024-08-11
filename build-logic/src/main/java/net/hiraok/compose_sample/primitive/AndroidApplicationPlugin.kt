package net.hiraok.compose_sample.primitive

import com.android.build.gradle.AppExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.internal.impldep.org.bouncycastle.asn1.iana.IANAObjectIdentifiers.experimental
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AndroidApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("com.google.devtools.ksp")
            }

            extensions.configure<AppExtension> {
                defaultConfig {
                    applicationId = "net.hiraok.compose_sample"
                    minSdk = 28
                    versionCode = 1
                    versionName = "1.0"

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    vectorDrawables {
                        useSupportLibrary = true
                    }
                }
                buildTypes {
                    getByName("debug") {
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
                buildFeatures.compose = true
                testOptions {
                    unitTests {
                        isIncludeAndroidResources = true
                    }
                }
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                add("implementation", libs.findLibrary("androidx-compose-navigation").get())
                add("implementation", libs.findLibrary("androidx-compose-material3").get())
                add("implementation", libs.findLibrary("androidx-compose-ui").get())
                add("implementation", libs.findLibrary("androidx-compose-ui-tooling").get())
                add("implementation", libs.findLibrary("androidx-activity-compose").get())
                add("implementation", libs.findLibrary("coil-kt").get())
                add("implementation", libs.findLibrary("coil-kt-compose").get())
                add("implementation", libs.findLibrary("timber").get())
                add("implementation", libs.findLibrary("preferences-core").get())
                add("implementation", libs.findLibrary("retrofit-core").get())
                add("implementation", libs.findLibrary("retrofit-kotlin-serialization").get())
                add("implementation", libs.findLibrary("kotlinx-serialization-json").get())
                add("implementation", libs.findLibrary("splashscreen").get())
                add("implementation", libs.findLibrary("okhttp").get())
                add("implementation", libs.findLibrary("okhttp-logging").get())
                add("implementation", libs.findLibrary("hilt-android").get())
                add("ksp", libs.findLibrary("hilt-android-compiler").get())
            }
        }
    }
}