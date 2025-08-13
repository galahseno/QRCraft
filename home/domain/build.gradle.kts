plugins {
    alias(libs.plugins.qrcraft.jvm.library)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(projects.core.domain)
}
