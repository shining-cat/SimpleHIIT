# Module Dependency Analysis & Recommendations

## Executive Summary

After migrating to shared-ui modules, several **redundant dependencies** exist in UI feature modules. The assertion rules need tightening to enforce the new architecture properly.

## Current Dependency Audit

### ✅ CLEAN: shared-ui Layer
All 4 shared-ui modules follow a perfect pattern:
- `shared-ui:home` → `commonUtils`, `domain:common`, `domain:home`
- `shared-ui:session` → `commonUtils`, `domain:common`, `domain:session`
- `shared-ui:settings` → `commonUtils`, `domain:common`, `domain:settings`
- `shared-ui:statistics` → `commonUtils`, `domain:common`, `domain:statistics`

**Pattern:** Each shared-ui feature depends on its corresponding domain feature + common modules. ✓

### ✅ CLEAN: domain Layer
- `domain:common` → `commonUtils` only
- All domain features → `commonUtils`, `domain:common`

**Pattern:** Pure domain layer, no cross-feature dependencies. ✓

### ✅ CLEAN: data Layer
- `data` → `domain:common`, `commonUtils`

**Pattern:** Data depends only on domain layer and utils. ✓

### ✅ CLEAN: Common Modules
- `commonResources` → `domain:common` (for type-safe mappers)
- `commonUtils` → NOTHING (foundation)
- `android:common` → `domain:common`, `commonUtils`, `commonResources`

**Pattern:** Proper hierarchy. ✓

### ⚠️ ISSUE: Mobile UI Features
**Current dependencies for each feature (e.g., home):**
```
android:mobile:ui:home → android:common
                       → domain:common          ← REDUNDANT
                       → domain:home            ← REDUNDANT
                       → commonUtils            ← REDUNDANT
                       → commonResources        ← REDUNDANT
                       → android:mobile:ui:common
                       → shared-ui:home
```

**Problem:** Direct dependencies on `domain:common`, `domain:home`, `commonUtils`, and `commonResources` are **redundant** because:
1. `shared-ui:home` already provides `domain:home` + `domain:common` + `commonUtils`
2. `android:mobile:ui:common` already provides `commonResources` + `commonUtils`

### ⚠️ ISSUE: TV UI Features
**Same redundancy pattern** as mobile.

## Critical Issues

### 1. Redundant Dependencies Break DRY Principle

**Current (redundant):**
```kotlin
// android/mobile/ui/home/build.gradle.kts
dependencies {
    implementation(projects.android.common)
    implementation(projects.domain.common)          // ← via shared-ui:home
    implementation(projects.domain.home)            // ← via shared-ui:home
    implementation(projects.commonUtils)            // ← via shared-ui:home
    implementation(projects.commonResources)        // ← via android:mobile:ui:common
    implementation(projects.android.mobile.ui.common)
    implementation(projects.sharedUi.home)
}
```

**Should be (DRY):**
```kotlin
// android/mobile/ui/home/build.gradle.kts
dependencies {
    implementation(projects.android.common)
    implementation(projects.android.mobile.ui.common)
    implementation(projects.sharedUi.home)
}
```

### 2. Current Rules Are Too Permissive

The rule `:android:mobile:ui:.* -> :domain:.*` allows UI modules to depend on **any** domain module, including:
- Cross-feature dependencies (e.g., home UI → session domain) ❌
- Direct domain access when shared-ui should mediate ❌

### 3. Missing Architectural Safeguards

Current rules don't prevent:
- UI modules depending on `:data` (violates clean architecture)
- shared-ui modules depending on Android-specific modules (blocks KMP migration)
- UI modules bypassing shared-ui to access domain directly

## Recommended Actions

### Action 1: Remove Redundant Dependencies

**From all mobile UI features**, remove:
- `implementation(projects.domain.common)`
- `implementation(projects.domain.<feature>)`
- `implementation(projects.commonUtils)`
- `implementation(projects.commonResources)`

**From all TV UI features**, remove same dependencies.

**Rationale:** These are already provided transitively through shared-ui and platform UI common modules.

### Action 2: Tighten Assertion Rules

**Remove these overly permissive rules:**
```kotlin
":android:mobile:ui:.* -> :domain:.*"
":android:tv:ui:.* -> :domain:.*"
```

**Replace with explicit allowances:**
```kotlin
// UI Common modules can depend on domain:common (for shared utilities)
":android:mobile:ui:common -> :domain:common"
":android:tv:ui:common -> :domain:common"

// But feature UI modules should NOT directly depend on domain
// (they get domain access through shared-ui)
```

### Action 3: Add Missing Restrictions

**Add to restricted rules:**
```kotlin
// UI modules CANNOT depend on data layer
":android:mobile:ui:.* -X> :data"
":android:tv:ui:.* -X> :data"

// shared-ui CANNOT depend on Android-specific modules (KMP-ready)
":shared-ui:.* -X> :android:.*"

// UI features CANNOT cross-depend on other domain features
":android:mobile:ui:home -X> :domain:session"
":android:mobile:ui:home -X> :domain:settings"
":android:mobile:ui:home -X> :domain:statistics"
// ... (repeat for all features)
```

### Action 4: Document New Architecture Pattern

Update MODULE_DEPENDENCIES.md to clarify:

**New Dependency Flow:**
```
┌──────────────────┐
│   UI Features    │ ← Platform-specific UI (Compose components)
│ (mobile/TV)      │   Depends on: shared-ui:feature + ui:common
└────────┬─────────┘
         │
┌────────▼─────────┐
│  shared-ui       │ ← Shared business logic (ViewModels, Interactors)
│  (KMP-ready)     │   Depends on: domain:feature + commonUtils
└────────┬─────────┘
         │
┌────────▼─────────┐
│   Domain Layer   │ ← Pure business logic
│  (feature/common)│   Depends on: domain:common + commonUtils
└────────┬─────────┘
         │
┌────────▼─────────┐
│   Data Layer     │ ← Data sources (Room, DataStore)
│                  │   Depends on: domain:common + commonUtils
└──────────────────┘
```

## Proposed Refined Rules

### Mobile App (android/mobile/app/build.gradle.kts)

```kotlin
allowed = arrayOf(
    // App can depend on anything
    ":android:mobile:app -> .*",

    // Mobile UI feature modules
    ":android:mobile:ui:home -> :android:common",
    ":android:mobile:ui:home -> :android:mobile:ui:common",
    ":android:mobile:ui:home -> :shared-ui:home",
    ":android:mobile:ui:session -> :android:common",
    ":android:mobile:ui:session -> :android:mobile:ui:common",
    ":android:mobile:ui:session -> :shared-ui:session",
    ":android:mobile:ui:settings -> :android:common",
    ":android:mobile:ui:settings -> :android:mobile:ui:common",
    ":android:mobile:ui:settings -> :shared-ui:settings",
    ":android:mobile:ui:statistics -> :android:common",
    ":android:mobile:ui:statistics -> :android:mobile:ui:common",
    ":android:mobile:ui:statistics -> :shared-ui:statistics",

    // Mobile UI common module
    ":android:mobile:ui:common -> :android:common",
    ":android:mobile:ui:common -> :domain:common",
    ":android:mobile:ui:common -> :commonUtils",
    ":android:mobile:ui:common -> :commonResources",

    // shared-ui modules
    ":shared-ui:.* -> :domain:.*",
    ":shared-ui:.* -> :commonUtils",

    // Domain modules
    ":domain:home -> :domain:common",
    ":domain:home -> :commonUtils",
    ":domain:session -> :domain:common",
    ":domain:session -> :commonUtils",
    ":domain:settings -> :domain:common",
    ":domain:settings -> :commonUtils",
    ":domain:statistics -> :domain:common",
    ":domain:statistics -> :commonUtils",
    ":domain:common -> :commonUtils",

    // Data layer
    ":data -> :domain:common",
    ":data -> :commonUtils",

    // Common modules
    ":commonResources -> :domain:common",
    ":android:common -> :commonResources",
    ":android:common -> :domain:common",
    ":android:common -> :commonUtils",
)

restricted = arrayOf(
    // Domain CANNOT depend on data
    ":domain:.* -X> :data",

    // Domain CANNOT depend on UI
    ":domain:.* -X> :android:mobile:ui:.*",
    ":domain:.* -X> :shared-ui:.*",

    // Domain CANNOT depend on app
    ":domain:.* -X> :android:mobile:app",

    // Domain features CANNOT cross-depend
    ":domain:home -X> :domain:session",
    ":domain:home -X> :domain:settings",
    ":domain:home -X> :domain:statistics",
    ":domain:session -X> :domain:home",
    ":domain:session -X> :domain:settings",
    ":domain:session -X> :domain:statistics",
    ":domain:settings -X> :domain:home",
    ":domain:settings -X> :domain:session",
    ":domain:settings -X> :domain:statistics",
    ":domain:statistics -X> :domain:home",
    ":domain:statistics -X> :domain:session",
    ":domain:statistics -X> :domain:settings",

    // Data CANNOT depend on UI
    ":data -X> :android:mobile:ui:.*",
    ":data -X> :shared-ui:.*",

    // Data CANNOT depend on app
    ":data -X> :android:mobile:app",

    // Data CANNOT depend on domain features (only domain:common)
    ":data -X> :domain:home",
    ":data -X> :domain:session",
    ":data -X> :domain:settings",
    ":data -X> :domain:statistics",

    // shared-ui CANNOT depend on Android-specific modules (KMP-ready)
    ":shared-ui:.* -X> :android:.*",

    // shared-ui CANNOT depend on commonResources (Android-specific)
    ":shared-ui:.* -X> :commonResources",

    // UI CANNOT depend on data
    ":android:mobile:ui:.* -X> :data",

    // NO module can depend on app
    ":android:mobile:ui:.* -X> :android:mobile:app",
    ":shared-ui:.* -X> :android:mobile:app",
    ":commonUtils -X> :android:mobile:app",
    ":commonResources -X> :android:mobile:app",
    ":android:common -X> :android:mobile:app",

    // Foundation modules CANNOT depend on anything
    ":commonUtils -X> :.*",
    ":testUtils -X> :.*",
)
```

## Implementation Priority

1. **HIGH:** Remove redundant dependencies from all UI feature modules
2. **HIGH:** Update assertion rules to be more specific
3. **MEDIUM:** Add missing restrictions
4. **LOW:** Update documentation

## Benefits

✅ **Enforces clean architecture** - UI can't bypass shared-ui to access domain
✅ **Reduces coupling** - Fewer direct dependencies
✅ **KMP-ready** - shared-ui modules can't depend on Android-specific code
✅ **Prevents regressions** - Explicit rules catch architectural violations
✅ **Clearer intent** - Dependencies explicitly show architectural layers
✅ **Faster builds** - Fewer dependency edges to resolve
