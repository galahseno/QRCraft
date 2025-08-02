plugins {
    alias(libs.plugins.qrcraft.android.library)
}

android {
    namespace = "id.dev.core.data"
}

dependencies {
    implementation(projects.core.domain)

}