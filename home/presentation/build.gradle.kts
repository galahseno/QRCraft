plugins {
    alias(libs.plugins.qrcraft.android.library.compose)
}

android {
    namespace = "id.dev.home.presentation"
}

dependencies {
    with(projects) {
        implementation(core.presentation)
        implementation(core.domain)
        implementation(home.domain)
    }
}