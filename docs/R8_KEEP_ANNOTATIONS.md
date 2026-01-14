# R8 Keep Annotations

## Overview

R8 minification can incorrectly remove classes accessed via reflection or dynamic invocation. Use these annotations to mark classes that must be kept.

## Annotations

### `@KeepForKoin`

**When to use:**
- Koin module declaration files (e.g., `*ModuleKt`, `*ModuleKoinKt`)
- Classes with Koin-injected generic types that R8 can't trace
- Interfaces used as Koin injection points

**Example:**
```kotlin
@file:KeepForKoin

package fr.shiningcat.simplehiit.domain.common.di

val commonDomainModule = module { /* ... */ }
```

### `@KeepForCompose`

**When to use:**
- Files with `@Composable` screen functions called via navigation
- Navigation route sealed classes
- Data classes used as navigation arguments

**Example:**
```kotlin
@file:KeepForCompose

package fr.shiningcat.simplehiit.android.mobile.ui.home

@Composable
fun HomeScreen() { /* ... */ }
```

### ViewModels

ViewModels are kept automatically via base class matching. No annotation needed.

## When in Doubt

If a release build crashes with `ClassNotFoundException`, check if the class needs `@KeepForKoin` or `@KeepForCompose`.

## Location

Annotations defined in: `commonUtils/src/main/java/fr/shiningcat/simplehiit/commonutils/annotations/`
