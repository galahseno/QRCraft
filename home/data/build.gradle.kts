plugins {
    alias(libs.plugins.qrcraft.android.library)
}

android {
    namespace = "id.dev.home.data"
}

dependencies {
    implementation(libs.mlkit.barcode.scanning)
    with(projects) {
        implementation(core.domain)
        implementation(core.data)
        implementation(home.domain)
    }
}