plugins {
    alias(libs.plugins.qrcraft.android.library)
}

android {
    namespace = "id.dev.home.data"
}

dependencies {
    with(projects) {
        implementation(core.domain)
        implementation(core.data)
        implementation(home.domain)
    }
}