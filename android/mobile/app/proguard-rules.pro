# SimpleHIIT ProGuard Rules
# Targeted rules using custom annotations for Koin and Compose

# Keep source file names and line numbers for better crash reports
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep annotations themselves
-keep @interface fr.shiningcat.simplehiit.commonutils.annotations.KeepForKoin
-keep @interface fr.shiningcat.simplehiit.commonutils.annotations.KeepForCompose

# Keep classes/files annotated with @KeepForKoin (DI modules, injected classes)
-keep @fr.shiningcat.simplehiit.commonutils.annotations.KeepForKoin class * { *; }
-keepclasseswithmembers,includedescriptorclasses class * {
    @fr.shiningcat.simplehiit.commonutils.annotations.KeepForKoin <methods>;
}
-keepclasseswithmembers,includedescriptorclasses class * {
    @fr.shiningcat.simplehiit.commonutils.annotations.KeepForKoin <fields>;
}

# Keep classes/files annotated with @KeepForCompose (Composable screens, navigation)
-keep @fr.shiningcat.simplehiit.commonutils.annotations.KeepForCompose class * { *; }
-keepclasseswithmembers,includedescriptorclasses class * {
    @fr.shiningcat.simplehiit.commonutils.annotations.KeepForCompose <methods>;
}

# Kotlin metadata for reflection (needed by Koin and serialization)
-keepattributes *Annotation*, InnerClasses, Signature, Exception

# Keep Kotlin metadata
-keep class kotlin.Metadata { *; }

# Kotlinx Serialization
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault
-dontnote kotlinx.serialization.AnnotationsKt
-keep,includedescriptorclasses class fr.shiningcat.simplehiit.**$$serializer { *; }
-keepclassmembers class fr.shiningcat.simplehiit.** {
    *** Companion;
}
-keepclasseswithmembers class fr.shiningcat.simplehiit.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep enum classes (used in navigation and settings)
-keepclassmembers enum fr.shiningcat.simplehiit.** {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep data classes (often used as state/navigation args)
-keepclassmembers class fr.shiningcat.simplehiit.**.models.** {
    <fields>;
    <init>(...);
}

# AndroidX and Jetpack Compose - Don't warn about AndroidX internals
-dontwarn androidx.**

# ViewModel
-keep class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}
-keep class * extends androidx.lifecycle.AndroidViewModel {
    <init>(...);
}

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
