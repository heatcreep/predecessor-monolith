plugins {
    alias(libs.plugins.predcompanion.android.feature.impl)
}

android {
    namespace = "com.aowen.predcompanion.feature.auth.impl"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.resources)
    implementation(projects.feature.auth.api)
    implementation(projects.feature.home.api)

    testImplementation(projects.core.testing)
}
