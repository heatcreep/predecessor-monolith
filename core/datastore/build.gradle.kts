plugins {
    alias(libs.plugins.predcompanion.android.library)
    alias(libs.plugins.predcompanion.hilt)
}

android {
    namespace = "com.aowen.monolith.core.datastore"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    api(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.serialization.json)
}
