import com.android.build.api.variant.BuildConfigField
import java.io.StringReader
import java.util.Properties

plugins {
    alias(libs.plugins.predcompanion.android.library)
    alias(libs.plugins.predcompanion.hilt)
    alias(libs.plugins.apollo.graphql)
    id("kotlinx-serialization")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
    }
}

android {
    buildFeatures {
        buildConfig = true
    }
    namespace = "com.aowen.predcompanion.core.network"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))

    implementation(libs.kotlinx.serialization.json)
    api(libs.retrofit.core)

    // Supabase
    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.auth)
    implementation(libs.supabase.postgres)
    implementation(libs.supabase.functions)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.serialization.converter)

    // AppAuth
    implementation("net.openid:appauth:0.11.1")

    // apollo
    api(libs.apollo.runtime)

}

val predGgToken = providers.fileContents(
    isolated.rootProject.projectDirectory.file("local.properties")
).asText.map { text ->
    val properties = Properties()
    properties.load(StringReader(text))
    properties["PRED_GG_TOKEN"] as String
}.orElse("")

apollo {
    service("predgg") {
        packageName.set("com.aowen.predcompanion.core.network.apollo")
        introspection {
            headers.put("Authorization", predGgToken.map { "Bearer $it" })
            endpointUrl.set("https://pred.gg/gql")
            schemaFile.set(file("src/main/graphql/schema.graphqls"))
        }
        mapScalar("DateTime", "kotlin.time.Instant")
    }
}

val supabaseApiKey = providers.fileContents(
    isolated.rootProject.projectDirectory.file("local.properties")
).asText.map { text ->
    val properties = Properties()
    properties.load(StringReader(text))
    properties["SUPABASE_KEY"]
}.orElse("supabaseKey")

val supabaseUrl = providers.fileContents(
    isolated.rootProject.projectDirectory.file("local.properties")
).asText.map { text ->
    val properties = Properties()
    properties.load(StringReader(text))
    properties["SUPABASE_URL"]
}.orElse("http://example.com")

val authBaseUrl = providers.fileContents(
    isolated.rootProject.projectDirectory.file("local.properties")
).asText.map { text ->
    val properties = Properties()
    properties.load(StringReader(text))
    properties["AUTH_BASE_URL"]
}.orElse("http://example.com")



androidComponents {
    onVariants {
        it.buildConfigFields!!.put("SUPABASE_API_KEY", supabaseApiKey.map { value ->
            BuildConfigField(type = "String", value = "$value", comment = null)
        })
        it.buildConfigFields!!.put("SUPABASE_URL", supabaseUrl.map { value ->
            BuildConfigField(type = "String", value = "$value", comment = null)
        })
        it.buildConfigFields!!.put("AUTH_BASE_URL", authBaseUrl.map { value ->
            BuildConfigField(type = "String", value = "$value", comment = null)
        })

    }
}
