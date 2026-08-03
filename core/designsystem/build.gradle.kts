plugins {
    alias(libs.plugins.predcompanion.android.library)
    alias(libs.plugins.predcompanion.android.library.compose)
}

android {
    namespace = "com.aowen.predcompanion.core.designsystem"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:resources"))
    implementation(projects.core.datastore)

    implementation(libs.androidx.core.ktx)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material3.adaptive)
    api(libs.androidx.compose.material3.navigationSuite)
    api(libs.androidx.compose.material.icons.extended)
    api(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.coil.compose)
    api(libs.coil.network.okhttp)
}
