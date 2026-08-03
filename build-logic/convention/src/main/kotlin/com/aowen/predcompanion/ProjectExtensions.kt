package com.aowen.predcompanion

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import java.util.Properties

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun Project.getEnvironmentVariable(key: String): String? {
    val isCi = System.getenv("CI")?.toBoolean() ?: false
    return if (isCi) {
        System.getenv(key)
    } else {
        val properties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            properties.load(localPropertiesFile.inputStream())
            properties.getProperty(key)
        } else {
            null
        }
    }
}

fun Project.getEnvironmentVariableAsString(key: String, defaultValue: String = ""): String {
    return "\"${getEnvironmentVariable(key) ?: defaultValue}\""
}