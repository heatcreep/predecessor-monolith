import com.android.build.api.variant.BuildConfigField
import com.aowen.predcompanion.getEnvironmentVariable

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

val predGgToken = project.provider {
    project.getEnvironmentVariable("PRED_GG_TOKEN") ?: ""
}

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

val authBaseUrl = project.provider {
    project.getEnvironmentVariable("AUTH_BASE_URL") ?: "http://example.com"
}



androidComponents {
    onVariants {
        it.buildConfigFields!!.put("AUTH_BASE_URL", authBaseUrl.map { value ->
            BuildConfigField(type = "String", value = "\"$value\"", comment = null)
        })
    }
}
