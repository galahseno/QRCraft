plugins {
    alias(libs.plugins.qrcraft.android.library.compose)
}


android {
    namespace = "id.dev.core.presentation"
}

dependencies {
    implementation(projects.core.domain)
}