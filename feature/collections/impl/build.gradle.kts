plugins {
    alias(libs.plugins.predcompanion.android.feature.impl)
}

android {
    namespace = "com.aowen.predcompanion.feature.collections.impl"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.resources)
    implementation(projects.feature.collections.api)
    implementation(projects.feature.builds.api)
    implementation(projects.feature.search.api)

    testImplementation(projects.core.testing)

}
