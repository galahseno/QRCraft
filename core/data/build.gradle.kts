plugins {
    alias(libs.plugins.qrcraft.android.library)
    id("androidx.room")
    id("com.google.devtools.ksp")
}

android {
    namespace = "id.dev.core.data"
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    implementation(projects.core.domain)
    implementation("androidx.room:room-runtime:2.7.2")
    ksp("androidx.room:room-compiler:2.7.2")
    implementation("androidx.room:room-ktx:2.7.2")
    implementation("androidx.room:room-paging:2.7.2")
}