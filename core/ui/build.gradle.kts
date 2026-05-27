plugins {
    alias(libs.plugins.predcompanion.android.library)
    alias(libs.plugins.predcompanion.android.library.compose)
    alias(libs.plugins.predcompanion.hilt)
}

android {
    namespace = "com.aowen.predcompanion.core.ui"
}

dependencies {
    api(projects.core.designsystem)
    api(projects.core.model)
    api(projects.core.resources)
    implementation(projects.core.data)
    implementation(project(":core:datastore"))

    implementation(libs.coil.compose)
}
