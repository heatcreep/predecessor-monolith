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

val authBaseUrl = providers.fileContents(
    isolated.rootProject.projectDirectory.file("local.properties")
).asText.map { text ->
    val properties = Properties()
    properties.load(StringReader(text))
    properties["AUTH_BASE_URL"]
}.orElse("http://example.com")



androidComponents {
    onVariants {
        it.buildConfigFields!!.put("AUTH_BASE_URL", authBaseUrl.map { value ->
            BuildConfigField(type = "String", value = "$value", comment = null)
        })
    }
}
