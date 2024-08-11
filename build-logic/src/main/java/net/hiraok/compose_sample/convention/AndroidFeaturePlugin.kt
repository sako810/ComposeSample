package net.hiraok.compose_sample.convention

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("net.hiraok.library")
                apply("net.hiraok.compose")
            }
        }
    }
}