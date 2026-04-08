
import java.util.Properties

plugins {
    alias(libs.plugins.predcompanion.android.application)
    alias(libs.plugins.predcompanion.android.application.compose)
    alias(libs.plugins.predcompanion.android.application.firebase)
    alias(libs.plugins.predcompanion.hilt)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.aowen.predcompanion"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.aowen.predcompanion"
        minSdk = 24
        targetSdk = 36
        versionCode = (System.getenv("VERSION_CODE")?.toIntOrNull() ?: 1) + 1
        versionName = System.getenv("VERSION_NAME") ?: "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "SUPABASE_URL", "${getEnvironmentVariable("SUPABASE_URL")}")
        buildConfigField("String", "SUPABASE_API_KEY", "${getEnvironmentVariable("SUPABASE_KEY")}   ")
    }
    signingConfigs {
        register("release") {
            if (System.getenv("KEYSTORE_FILE") != null) {
                storePassword = System.getenv("SIGNING_STORE_PASSWORD")
                keyAlias = System.getenv("SIGNING_KEY_ALIAS")
                storeFile = file(System.getenv("KEYSTORE_FILE"))
                keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
            }
        }
    }
    buildTypes {
        debug {
            isDebuggable = true
        }
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = if (signingConfigs.getByName("release").storeFile != null) {
                signingConfigs.getByName("release")
            } else {
                signingConfigs.getByName("debug")
            }
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

fun getLocalProperty(key: String): String? {
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())
    return properties.getProperty(key)
}

fun getEnvironmentVariable(key: String): String? {
    val isCi = System.getenv("CI")?.toBoolean() ?: false
    return if (isCi) {
        System.getenv(key)
    } else {
        getLocalProperty(key)
    }
}

dependencies {

    // Feature modules
    implementation(projects.feature.auth.api)
    implementation(projects.feature.auth.impl)
    implementation(projects.feature.builds.api)
    implementation(projects.feature.builds.impl)
    implementation(projects.feature.heroes.api)
    implementation(projects.feature.heroes.impl)
    implementation(projects.feature.home.api)
    implementation(projects.feature.home.impl)
    implementation(projects.feature.items.api)
    implementation(projects.feature.items.impl)
    implementation(projects.feature.matches.api)
    implementation(projects.feature.matches.impl)
    implementation(projects.feature.profile.api)
    implementation(projects.feature.profile.impl)
    implementation(projects.feature.search.api)
    implementation(projects.feature.search.impl)

    // Core modules
    implementation(projects.core.analytics)
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.database)
    implementation(projects.core.datastore)
    implementation(projects.core.designsystem)
    implementation(projects.core.model)
    implementation(projects.core.navigation)
    implementation(projects.core.network)
    implementation(projects.core.resources)
    implementation(projects.core.ui)

    // Android / Kotlin

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.kotlinx.immutable.collections)

    // Compose
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.navigation)

    // Navigation
    implementation(libs.androidx.navigation3.ui)

    // Glance (App Widget)
    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.glance.material3)

    // Hilt
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.work)

    // Markdown (used by feature screens still in app)
    implementation(libs.meetup.markdown)


    // Image loading (used by feature screens still in app)
    implementation(libs.coil.compose)

    // Serialization + Retrofit (AppModule.kt creates Json & Retrofit instances)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.serialization.converter)

    // Supabase (AppModule.kt creates SupabaseClient)
    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.gotrue)
    implementation(libs.supabase.postgres)
    implementation(libs.supabase.functions)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.okhttp)

    // WorkManager
    implementation(libs.androidx.work)

    // Test
    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.paging.testing)
    testImplementation(libs.androidx.paging.testing.android)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test)
    debugImplementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.test)
}
