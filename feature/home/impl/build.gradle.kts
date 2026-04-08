plugins {
    alias(libs.plugins.predcompanion.android.feature.impl)
}

android {
    namespace = "com.aowen.predcompanion.feature.home.impl"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.feature.builds.api)
    implementation(projects.feature.heroes.api)
    implementation(projects.feature.home.api)
    implementation(projects.feature.matches.api)
    implementation(projects.feature.search.api)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
}
