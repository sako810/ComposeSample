package net.hiraok.compose_sample.primitive

import com.android.build.gradle.TestedExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType


fun Project.androidApplication(action: BaseAppModuleExtension.() -> Unit) {
    extensions.configure<BaseAppModuleExtension>(action)
}

fun Project.android(action: TestedExtension.() -> Unit) {
    extensions.configure<TestedExtension>(action)
}

val Project.libs get() = extensions.getByType<VersionCatalogsExtension>().named("libs")
