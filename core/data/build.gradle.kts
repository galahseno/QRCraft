plugins {
    alias(libs.plugins.qrcraft.android.library)
    alias(libs.plugins.ksp)
}

android {
    namespace = "id.dev.core.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.bundles.room)

    ksp(libs.room.compiler)
}