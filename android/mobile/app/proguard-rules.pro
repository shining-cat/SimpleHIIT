# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep source file names and line numbers for better crash reports
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep Koin module declarations (accessed via reflection)
-keep class * extends org.koin.core.module.Module { *; }
-keep class **.*ModuleKt { *; }
-keep class **.*ModuleKoinKt { *; }

# Keep all ViewModels (injected by Koin)
-keep class * extends androidx.lifecycle.ViewModel { *; }

# Keep Compose screen functions (called via navigation)
-keep class **.*ScreenKt { *; }

# Keep navigation Screen sealed class and subclasses
-keep class fr.shiningcat.**.android.shared.core.Screen { *; }
-keep class fr.shiningcat.**.android.shared.core.Screen$* { *; }

# Keep domain models used in ViewModels
-keep class fr.shiningcat.**.domain.common.Output { *; }
-keep class fr.shiningcat.**.domain.common.Output$* { *; }
-keep class fr.shiningcat.**.domain.common.models.** { *; }
-keep class fr.shiningcat.**.domain.common.DurationFormatPatterns { *; }

# Keep use cases (injected by Koin)
-keep class fr.shiningcat.**.domain.**.usecases.** { *; }

# Keep utility classes injected by Koin
-keep class fr.shiningcat.**.commonutils.HiitLogger { *; }
-keep class fr.shiningcat.**.commonutils.AndroidVersionProvider { *; }
-keep class fr.shiningcat.**.commonutils.AndroidVersionProviderImpl { *; }

# Keep data layer interfaces injected by Koin
-keep interface fr.shiningcat.**.data.local.localemanager.LocaleManager { *; }
-keep class fr.shiningcat.**.data.local.localemanager.LocaleManager { *; }

# Keep UI arrangement enum (used in Compose)
-keep class fr.shiningcat.**.android.mobile.ui.common.UiArrangement { *; }

# Keep theme and window helpers (used in Compose)
-keep class **.theme.*ThemeKt { *; }
-keep class fr.shiningcat.**.ui.common.theme.** { *; }
-keep class fr.shiningcat.**.ui.common.helpers.** { *; }

# Kotlin metadata for reflection
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
