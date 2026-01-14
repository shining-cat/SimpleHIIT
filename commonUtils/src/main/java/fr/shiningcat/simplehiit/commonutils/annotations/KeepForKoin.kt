package fr.shiningcat.simplehiit.commonutils.annotations

/**
 * Marks classes that must be kept for Koin dependency injection.
 *
 * Apply to:
 * - Koin module objects and their containing files
 * - Classes injected with generic types that R8 might strip
 * - Interfaces used as Koin injection points
 *
 * R8 cannot detect reflection-based access patterns used by Koin,
 * so these classes would be incorrectly removed during minification.
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class KeepForKoin
