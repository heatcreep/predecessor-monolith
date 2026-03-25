plugins {
    alias(libs.plugins.predcompanion.android.library)
    alias(libs.plugins.predcompanion.hilt)
}

android {
    namespace = "com.aowen.monolith.core.data"
}

dependencies {
    api(project(":core:common"))
    api(project(":core:database"))
    api(project(":core:datastore"))
    api(project(":core:network"))
    api(project(":core:model"))

    implementation(libs.retrofit.core)

    // Supabase (for SessionStatus reference in AuthRepository)
    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.gotrue)
}
