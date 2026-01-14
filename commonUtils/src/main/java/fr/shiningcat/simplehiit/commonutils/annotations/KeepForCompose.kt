package fr.shiningcat.simplehiit.commonutils.annotations

/**
 * Marks classes that must be kept for Jetpack Compose navigation or composition.
 *
 * Apply to:
 * - Files containing @Composable screen functions called via navigation
 * - Data classes used as navigation arguments
 * - Sealed classes representing navigation routes
 *
 * Compose navigation uses string-based references and lambda invocations
 * that R8 cannot trace, causing these classes to be incorrectly removed.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class KeepForCompose
