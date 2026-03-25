plugins {
    alias(libs.plugins.predcompanion.android.library)
    alias(libs.plugins.predcompanion.android.library.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.aowen.monolith.core.navigation"
}

dependencies {
    // TODO: Remove
    api(libs.androidx.compose.navigation)
    api(libs.androidx.hilt.navigation.compose)

    api(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.savedstate.compose)
    implementation(libs.androidx.lifecycle.viewModel.navigation3)
}
