plugins {
    alias(libs.plugins.predcompanion.android.feature.api)
    alias(libs.plugins.predcompanion.android.library.compose)
}

android {
    namespace = "com.aowen.predcompanion.feature.matches.api"
}

dependencies {
    implementation(projects.core.ui)
}