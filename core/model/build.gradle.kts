plugins {
    alias(libs.plugins.predcompanion.android.library)
    alias(libs.plugins.predcompanion.android.library.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.aowen.predcompanion.core.model"
}

dependencies {
    // Resources module provides shared drawables (R.drawable.*)
    implementation(project(":core:resources"))
    // Common utilities (RetrofitHelper, BigDecimalSerializer, etc.)
    implementation(project(":core:common"))

    implementation(libs.kotlinx.serialization.json)
}
