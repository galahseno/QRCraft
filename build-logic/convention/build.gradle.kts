plugins {
    `kotlin-dsl`
}

group = "id.dev.build-logic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApp") {
            id = "qrcraft.android.application"
            implementationClass = "AndroidAppConventionPlugin"
        }
        register("androidLib") {
            id = "qrcraft.android.library"
            implementationClass = "AndroidLibConventionPlugin"
        }
        register("androidLibCompose") {
            id = "qrcraft.android.library.compose"
            implementationClass = "AndroidLibComposeConventionPlugin"
        }
        register("jvmLibrary") {
            id = "qrcraft.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}