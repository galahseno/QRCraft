plugins {
    alias(libs.plugins.qrcraft.android.library.compose)
}

android {
    namespace = "id.dev.home.presentation"
}

dependencies {
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    with(projects) {
        implementation(core.presentation)
        implementation(core.domain)
        implementation(home.domain)
    }
}