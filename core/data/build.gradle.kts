plugins {
    alias(libs.plugins.predcompanion.android.library)
    alias(libs.plugins.predcompanion.android.library.compose)
    alias(libs.plugins.predcompanion.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.aowen.monolith.core.data"
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
    }
}

dependencies {
    api(projects.core.common)
    api(projects.core.database)
    api(projects.core.datastore)
    api(projects.core.model)
    api(projects.core.network)
    api(projects.core.resources)

    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    implementation("net.openid:appauth:0.11.1")


    testImplementation(projects.core.testing)
}
