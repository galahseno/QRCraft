plugins {
    alias(libs.plugins.qrcraft.android.library.compose)
}

android {
    namespace = "id.dev.home.presentation"
}

dependencies {
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.timber)
    implementation(libs.bundles.camerax)
    with(projects) {
        implementation(core.presentation)
        implementation(core.domain)
        implementation(home.domain)
    }
}