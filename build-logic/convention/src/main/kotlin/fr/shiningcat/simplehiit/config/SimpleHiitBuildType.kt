package fr.shiningcat.simplehiit.config

/**
 * Centralized build type configuration for the entire project.
 * Single source of truth for R8, testing, and build variant settings.
 */
enum class SimpleHiitBuildType(
    val isMinifyEnabled: Boolean,
    val enableUnitTestCoverage: Boolean,
    val applicationIdSuffix: String? = null,
) {
    DEBUG(
        isMinifyEnabled = false,
        enableUnitTestCoverage = true,
        applicationIdSuffix = ".debug",
    ),
    RELEASE(
        isMinifyEnabled = true,
        enableUnitTestCoverage = false,
    ),
}
