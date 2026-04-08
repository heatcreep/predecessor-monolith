plugins {
    alias(libs.plugins.predcompanion.android.library)
    alias(libs.plugins.predcompanion.hilt)
}

android {
    namespace = "com.aowen.predcompanion.core.common"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    api(libs.retrofit.core)
}
