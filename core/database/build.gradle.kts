plugins {
    alias(libs.plugins.predcompanion.android.library)
    alias(libs.plugins.predcompanion.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.aowen.predcompanion.core.database"
}

dependencies {
    implementation(projects.core.model)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.kotlinx.serialization.json)
}
