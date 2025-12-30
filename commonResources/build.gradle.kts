plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.testing)
}

android {
    namespace = "fr.shiningcat.simplehiit.commonresources"
}

dependencies {
    implementation(projects.domain.common)
}
