plugins {
    alias(libs.plugins.predcompanion.android.feature.impl)
    alias(libs.plugins.predcompanion.android.library)
    alias(libs.plugins.predcompanion.android.library.compose)
    alias(libs.plugins.predcompanion.hilt)
}

android {
    namespace = "com.aowen.monolith.feature.heroes"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:designsystem"))

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.compose.navigation)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
}
