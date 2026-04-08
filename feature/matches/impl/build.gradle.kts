plugins {
    alias(libs.plugins.predcompanion.android.feature.impl)
}

android {
    namespace = "com.aowen.predcompanion.feature.matches.impl"
}

dependencies {
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    implementation(projects.core.data)
    implementation(projects.core.resources)

    implementation(projects.feature.matches.api)
    implementation(projects.feature.home.api)
}
