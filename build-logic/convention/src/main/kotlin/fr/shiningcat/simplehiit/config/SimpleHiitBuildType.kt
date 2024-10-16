package fr.shiningcat.simplehiit.config

enum class SimpleHiitBuildType(
    val applicationIdSuffix: String? = null,
    val isMinifyEnabled: Boolean,
    val enableUnitTestCoverage: Boolean,
) {
    DEBUG(
        applicationIdSuffix = ".debug",
        isMinifyEnabled = false,
        enableUnitTestCoverage = true,
    ),
    RELEASE(
        isMinifyEnabled = true,
        enableUnitTestCoverage = false,
    ),
}
