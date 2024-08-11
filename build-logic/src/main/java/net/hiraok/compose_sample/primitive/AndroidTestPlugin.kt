package net.hiraok.compose_sample.primitive

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidTestPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("io.github.takahirom.roborazzi")
            }
            dependencies {
                add("testImplementation", libs.findLibrary("junit").get())
                add("testImplementation", libs.findLibrary("androidx-junit").get())
                add("testImplementation", libs.findLibrary("roborazzi").get())
                add("testImplementation", libs.findLibrary("roborazzi-compose").get())
                add("testImplementation", libs.findLibrary("roborazzi-junit-rules").get())
                add("testImplementation", libs.findLibrary("robolectric").get())
                add("testImplementation", libs.findLibrary("androidx-compose-ui-test-junit4").get())
                add("testImplementation", libs.findLibrary("androidx-compose-ui-test").get())
            }
        }
    }
}