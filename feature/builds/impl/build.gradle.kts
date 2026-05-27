plugins {
    alias(libs.plugins.predcompanion.android.feature.impl)
    alias(libs.plugins.predcompanion.android.library.compose)
}

android {
    namespace = "com.aowen.predcompanion.feature.builds.impl"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.feature.builds.api)
    implementation(projects.feature.items.api)
    implementation(projects.feature.profile.api)
    implementation(projects.feature.search.api)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
    implementation(libs.kotlinx.immutable.collections)
    implementation(libs.meetup.markdown)

    testImplementation(projects.core.testing)
    testImplementation(libs.androidx.paging.testing)
}
