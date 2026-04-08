plugins {
    alias(libs.plugins.predcompanion.android.feature.impl)
}

android {
    namespace = "com.aowen.predcompanion.feature.items.impl"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.resources)
    implementation(projects.feature.items.api)
    implementation(projects.feature.search.api)
}
