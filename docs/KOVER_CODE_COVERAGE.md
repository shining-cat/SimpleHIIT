# Kover Code Coverage

## What is Kover?

[Kover](https://github.com/Kotlin/kotlinx-kover) is a code coverage tool for Kotlin, developed by
JetBrains. It provides accurate coverage metrics for Kotlin code, with native support for Kotlin
language features like coroutines, inline functions, and sealed classes.

- **GitHub:** https://github.com/Kotlin/kotlinx-kover
- **Documentation:** https://kotlin.github.io/kotlinx-kover/

### Why Kover over JaCoCo?

- **Kotlin-native:** Built specifically for Kotlin by JetBrains
- **Better accuracy:** Handles Kotlin language features correctly (inline functions, coroutines,
  sealed classes)
- **Active development:** Regular updates from the Kotlin team
- **Modern API:** Clean Gradle configuration with Kotlin DSL support

## Setup in This Project

### Version

We use **Kover 0.9.3** (latest stable as of migration).

See `gradle/libs.versions.toml`:

```toml
kover = "0.9.3"
```

### Project Structure

```
build.gradle.kts (root)          # Global configuration & exclusions
├── android/mobile/app/          # Kover plugin applied
├── data/                        # Kover plugin applied
├── domain/common/               # Kover plugin applied
└── ... (other modules)          # Kover plugin applied
└── ... (other modules)          # Add kover plugin as needed
```

### Adding Kover to a Module

To enable coverage for a module, add the plugin to its `build.gradle.kts`:

```kotlin
plugins {
    // ... existing plugins
    alias(libs.plugins.kover)
}
```

and add it to the project level build dependencies block:

```kotlin
dependencies {
    // ... existing plugins
    kover(project(":path:to:module"))
}
```

The module will automatically:

- Inherit global exclusions from root
- Participate in aggregated coverage reports
- Clean reports when `./gradlew clean` is run

## Running Coverage Reports

### Generate Coverage

**From IDE (Gradle Tasks Panel):**

1. (optional, first Run `testDebugUnitTest` (runs all debug unit tests))
2. Run `koverHtmlReport` (runs testDebugUnitTest if needed then generates HTML coverage report)

**From Command Line:**

```bash
# Full project coverage
./gradlew testDebugUnitTest koverHtmlReport

# Single module coverage
./gradlew :data:testDebugUnitTest :data:koverHtmlReport

# XML report (for CI/CD)
./gradlew testDebugUnitTest koverXmlReport
```

### Report Locations

**Aggregated (all modules):**

```
build/reports/kover/html/index.html
```

**Per-Module:**

```
module-name/build/reports/kover/html/index.html
```

For example:

- `data/build/reports/kover/html/index.html`
- `domain/common/build/reports/kover/html/index.html`

### Cleaning Reports

Reports are automatically deleted when running clean:

```bash
# Clean root and all subprojects
./gradlew clean

# Clean specific module
./gradlew :data:clean
```

This removes:

- `build/reports/kover/` (root aggregated report)
- `module/build/reports/kover/` (module-specific reports)

## Global Exclusions (Root Level)

Global exclusions are configured in `build.gradle.kts` and apply to **all modules**.

### Current Global Exclusions

**Annotations:**

```kotlin
annotatedBy("*Generated")
annotatedBy("javax.annotation.processing.Generated")
annotatedBy("javax.annotation.Generated")
annotatedBy("dagger.internal.DaggerGenerated")
```

**Classes:**

```kotlin
// Android generated
classes("*.BuildConfig")
classes("*.R")
classes("*.R$*")

// DataBinding
classes("*.databinding.*")
classes("*.DataBindingInfo")

// DTOs
classes("*DTO")

// Dagger/Hilt (⚠️ see caveat below)
classes("*.*_Factory")
classes("*.*_MembersInjector")
classes("*.*Hilt_*")
classes("*.Hilt_*")

// Room
classes("*.*_Impl")
```

**Packages:**

```kotlin
// Hilt generated
packages("dagger.hilt.internal.aggregatedroot.codegen")

// All DI modules
packages("*.di")

// All model packages
packages("*.models")
```

### ⚠️ Known Limitation: Dagger Factory Classes

**Issue:** Dagger-generated Factory classes (`*_Factory`) are **not being excluded** despite the
configuration above.

**Affected classes:**

- `AndroidVersionProviderImpl_Factory`
- `UserRepositoryImpl_Factory`
- etc.

**Root cause:** This appears to be a limitation in Kover 0.9.3's pattern matching for certain
generated classes.

**Status:** We've kept the patterns in the configuration for when/if this gets fixed in future Kover
versions.

## Module-Level Exclusions

Module-level exclusions apply **only to that module's report**, but also affect the aggregated root
report.

### When to Use Module-Level Exclusions

- Module-specific generated code
- Legacy/experimental code in a specific module
- Module-specific frameworks or patterns

### Configuration

In the module's `build.gradle.kts`, add:

```kotlin
plugins {
    alias(libs.plugins.kover)
}

kover {
    reports {
        filters {
            excludes {
                // Your exclusions here
            }
        }
    }
}
```

### Example:  Exclusions

```kotlin
kover {
    reports {
        filters {
            excludes {
                // Exclude by package
                packages("fr.shiningcat.simplehiit.domain.common.models")

                // Exclude by class pattern
                classes("*DTO")
                classes("*Entity")

                // Exclude specific classes
                classes("fr.shiningcat.simplehiit.data.SpecificClass")

                // Exclude by annotation
                annotatedBy("com.example.ExcludeFromCoverage")
            }
        }
    }
}
```

## Best Practices

### What to Exclude

✅ **Should be excluded:**

- Data classes / DTOs / Models
- Generated code (Hilt, Room, DataBinding)
- DI configuration modules
- Build configuration classes
- Simple mappers with no business logic

❌ **Should NOT be excluded:**

- Business logic
- Use cases
- Repositories (with actual logic)
- ViewModels
- Utility classes with algorithms

### Understanding Coverage Metrics

**Coverage measures execution, not testing quality.**

- A data class can show 100% coverage just from being used in other tests
- Coverage doesn't guarantee correctness - only that code was executed
- Focus on coverage of critical business logic, not overall percentage
- Aim for high coverage of branching logic (if/when/try-catch)

### Tips for Accurate Coverage

1. **Run clean before coverage** to ensure fresh data
   ```bash
   ./gradlew clean testDebugUnitTest koverHtmlReport
   ```

2. **Check branch coverage**, not just line coverage
    - Look for uncovered `if/else` branches
    - Ensure all `when` cases are tested

3. **Focus on meaningful tests**
    - Coverage doesn't replace good test design
    - Test behaviors, not just execution

4. **Review module reports** for detailed analysis
    - Root report shows overview
    - Module reports show file-level detail

## Troubleshooting

### Reports Not Generated

**Issue:** No HTML report after running koverHtmlReport

**Solution:**

1. Ensure tests ran first: `./gradlew testDebugUnitTest`
2. Check module has Kover plugin applied
3. Check module is listed in root `dependencies { kover(project(...)) }`

### R8 Minification Errors

**Issue:** Build fails with R8 errors when generating coverage

**Solution:**
This is already handled - we disable coverage for release variants. Always use debug tasks:

- ✅ `testDebugUnitTest` + `koverHtmlReport`
- ❌ `test` (tries to run release tests)

### Exclusions Not Working

**Issue:** Classes still appear in report despite exclusions

**Try:**

1. Clean and regenerate: `./gradlew clean testDebugUnitTest koverHtmlReport`
2. Use full package path instead of wildcards
3. Check if it's the Factory class limitation (see above)
4. Verify exclusion is in correct location (root vs module)

## Additional Resources

- **Kover Documentation:** https://kotlin.github.io/kotlinx-kover/
- **Kover GitHub:** https://github.com/Kotlin/kotlinx-kover
- **Kover Gradle Plugin:** https://plugins.gradle.org/plugin/org.jetbrains.kotlinx.kover

## Migration Notes

This project was migrated from JaCoCo to Kover 0.9.3 on October 22, 2025.

**Key changes:**

- All modules now use Kover plugin directly
- Global exclusions centralized in root build.gradle.kts
- Reports location changed to `build/reports/kover/`
- Automatic cleanup on `./gradlew clean`
- Factory class exclusion limitation documented
