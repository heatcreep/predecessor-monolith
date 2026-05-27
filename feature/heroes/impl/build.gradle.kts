plugins {
    alias(libs.plugins.predcompanion.android.feature.impl)
}

android {
    namespace = "com.aowen.predcompanion.feature.heroes.impl"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.feature.builds.api)
    implementation(projects.feature.heroes.api)
    implementation(projects.feature.search.api)

    testImplementation(projects.core.testing)

}
