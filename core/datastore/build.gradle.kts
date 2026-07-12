plugins {
    alias(libs.plugins.predcompanion.android.library)
    alias(libs.plugins.predcompanion.hilt)
}

android {
    namespace = "com.aowen.predcompanion.core.datastore"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
    api(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.serialization.json)

    implementation("net.openid:appauth:0.11.1")
}
