/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.commonutils.annotations

/**
 * Marks classes or functions to be excluded from code coverage reports.
 *
 * Apply to:
 * - Trivial wrapper classes with no business logic (e.g., system API wrappers)
 * - Code that cannot be meaningfully tested (e.g., System.currentTimeMillis())
 * - Generated or framework-delegate code
 *
 * This annotation is used by Kover to filter out code that doesn't provide
 * meaningful coverage metrics from reports.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.FILE)
annotation class ExcludeFromCoverage
