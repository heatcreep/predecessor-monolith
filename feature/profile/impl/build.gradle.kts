import com.android.build.api.variant.BuildConfigField
import java.io.StringReader
import java.util.Properties

plugins {
    alias(libs.plugins.predcompanion.android.feature.impl)
}

android {
    namespace = "com.aowen.predcompanion.feature.profile.impl"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.designsystem)
    implementation(projects.core.resources)
    implementation(projects.feature.auth.api)
    implementation(projects.feature.search.api)
    implementation(projects.feature.profile.api)

    testImplementation(projects.core.testing)
}

val versionName = providers.fileContents(
    isolated.rootProject.projectDirectory.file("local.properties")
).asText.map { text ->
    val properties = Properties()
    properties.load(StringReader(text))
    properties["VERSION_NAME"]
}.orElse("http://example.com")

androidComponents {
    onVariants {
        it.buildConfigFields!!.put("VERSION_NAME", versionName.map { value ->
            BuildConfigField(type = "String", value = """"$value"""", comment = null)
        })
    }
}
