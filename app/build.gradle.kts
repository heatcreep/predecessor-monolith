
import java.util.Properties

plugins {
    alias(libs.plugins.predcompanion.android.application)
    alias(libs.plugins.predcompanion.android.application.compose)
    alias(libs.plugins.predcompanion.hilt)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.aowen.monolith"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.aowen.monolith"
        minSdk = 24
        targetSdk = 35
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
        register("firebaseDistribution") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
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

    // Core modules
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
    implementation(project(":core:data"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:navigation"))

    implementation(project(":feature:auth"))
    implementation(project(":feature:builds"))
    implementation(project(":feature:heroes"))
    implementation(project(":feature:home"))
    implementation(project(":feature:items"))
    implementation(project(":feature:profile"))

    // Android / Kotlin
    coreLibraryDesugaring(libs.android.desugarJdkLibs)
    implementation(platform(libs.kotlin.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.kotlinx.immutable.collections)

    // Navigation
    implementation(libs.androidx.compose.navigation)
    implementation(libs.androidx.navigation3.ui)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.app.distribution.api)
    "firebaseDistributionImplementation"(libs.firebase.app.distribution)

    // Glance (App Widget)
    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.glance.material3)

    // Accompanist (used by feature screens still in app)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.page.indictators)
    "firebaseDistributionImplementation"(libs.accompanist.permissions)

    // Hilt
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.work)
    ksp(libs.hilt.compiler)

    // Markdown (used by feature screens still in app)
    implementation(libs.meetup.markdown)

    // Paging (used by feature screens still in app)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

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
