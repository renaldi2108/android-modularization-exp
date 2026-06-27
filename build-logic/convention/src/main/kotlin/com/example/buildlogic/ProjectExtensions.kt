package com.example.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun Project.androidNamespace(): String =
    "com.example" + path.removePrefix(":").split(":").joinToString("") { ".$it" }

internal fun Project.featureDomainProjectPath(): String =
    path.substringBeforeLast(":") + ":domain"
