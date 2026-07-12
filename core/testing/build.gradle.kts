plugins {
    alias(libs.plugins.predcompanion.android.library)
    alias(libs.plugins.predcompanion.hilt)
}

android {
    namespace = "com.aowen.predcompanion.core.testing"
}

dependencies {
    api(libs.kotlinx.coroutines.test)
    api(projects.core.common)
    api(projects.core.data)
    api(projects.core.model)

    implementation(libs.androidx.test.rules)
    implementation(libs.hilt.android)
    implementation(libs.hilt.android.testing)
    implementation(libs.ktor.client.okhttp)
    api(libs.mockk)
    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.auth)
    implementation(libs.supabase.postgres)
}