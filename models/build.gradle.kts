plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.kover)
}

// Pure models module - no dependencies needed
dependencies {
    // No implementation dependencies - models are standalone
}
