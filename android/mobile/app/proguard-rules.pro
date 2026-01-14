# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep source file names and line numbers for better crash reports
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep classes marked with our custom annotations
-keep @fr.shiningcat.**.commonutils.annotations.KeepForKoin class * { *; }
-keep @fr.shiningcat.**.commonutils.annotations.KeepForCompose class * { *; }

# Keep all ViewModels (Koin injects them)
-keep class * extends androidx.lifecycle.ViewModel { *; }

# Kotlin metadata for reflection
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
