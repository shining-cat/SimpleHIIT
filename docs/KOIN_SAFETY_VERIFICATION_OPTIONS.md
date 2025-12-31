# Koin DI Safety Verification - Options Analysis

**Context:** Moving from Hilt (compile-time safety) to Koin (runtime DI) requires additional verification strategies to catch configuration errors early.

**Goal:** Establish multiple layers of safety checks to catch DI misconfigurations before they reach production.

---

## Quick Reference Matrix

| # | Option | Complexity | Safety Improvement | Dev Impact | Recommended |
|---|--------|------------|-------------------|------------|-------------|
| 1 | Unit Test: checkModules() | ⭐ Low | ⭐⭐⭐⭐⭐ High | ⭐ Minimal | ✅ **MUST HAVE** |
| 2 | Instrumented Test: Real Injection | ⭐⭐ Medium | ⭐⭐⭐⭐ High | ⭐ Minimal | ✅ **RECOMMENDED** |
| 3 | Koin Strict Mode (DSL) | ⭐ Low | ⭐⭐⭐ Medium | ⭐⭐ Low | ✅ **RECOMMENDED** |
| 4 | CI Pipeline: Verification Task | ⭐ Low | ⭐⭐⭐⭐ High | ⭐ None | ✅ **MUST HAVE** |
| 5 | Git Pre-commit Hook | ⭐⭐ Medium | ⭐⭐⭐ Medium | ⭐⭐⭐ Medium | ⚠️ **OPTIONAL** |
| 6 | Custom DSL Validator | ⭐⭐⭐⭐ High | ⭐⭐ Low | ⭐ Minimal | ❌ **NOT WORTH** |
| 7 | Gradle Custom Task | ⭐⭐ Medium | ⭐⭐⭐ Medium | ⭐ Minimal | ⚠️ **NICE TO HAVE** |
| 8 | ViewModel Scoped Tests | ⭐⭐ Medium | ⭐⭐⭐⭐ High | ⭐⭐ Low | ✅ **RECOMMENDED** |

---

## Option 1: Unit Test with checkModules() ⭐ PRIORITY 1

### Description
Koin provides `checkModules()` to verify all definitions can be created without actually instantiating them.

### Implementation
```kotlin
// testUtils/src/main/java/fr/shiningcat/simplehiit/testutils/KoinModulesTest.kt
class KoinModulesTest {
    @Test
    fun `verify Koin configuration`() {
        koinApplication {
            modules(allKoinModules)
            checkModules {
                withInstance(mockk<Context>(relaxed = true))
            }
        }
    }
}
```

**For Mobile and TV:**
```kotlin
// android/mobile/app/src/test/java/...
class MobileKoinConfigurationTest {
    @Test
    fun `verify mobile Koin modules are valid`() {
        koinApplication {
            modules(allKoinModules)
            checkModules {
                withInstance(mockk<Context>(relaxed = true))
            }
        }
    }
}

// android/tv/app/src/test/java/...
class TvKoinConfigurationTest {
    @Test
    fun `verify TV Koin modules are valid`() {
        koinApplication {
            modules(allKoinModules)
            checkModules {
                withInstance(mockk<Context>(relaxed = true))
            }
        }
    }
}
```

### Pros
- ✅ Catches missing dependencies immediately
- ✅ Runs in milliseconds (no Android emulator needed)
- ✅ Fails fast during development
- ✅ Standard Koin feature (well documented)
- ✅ No external dependencies
- ✅ Runs in CI automatically

### Cons
- ❌ Doesn't catch runtime injection failures in real scenarios
- ❌ May need mocks for Android-specific dependencies
- ❌ Won't catch qualifier mismatches if types align

### Metrics
- **Complexity:** ⭐ Low (15 lines of code)
- **Safety Improvement:** ⭐⭐⭐⭐⭐ High (catches 90% of issues)
- **Dev Impact:** ⭐ Minimal (runs with unit tests)
- **Execution Time:** ~50-200ms
- **False Positives:** Very Low

### Recommendation
**✅ MUST HAVE - Implement immediately**

---

## Option 2: Instrumented Test - Real Android Injection ⭐ PRIORITY 2

### Description
Test actual injection in Android environment with ViewModels, Activities, etc.

### Implementation
```kotlin
// android/mobile/app/src/androidTest/java/...
@RunWith(AndroidJUnit4::class)
class MobileDependencyInjectionTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger()
        modules(allKoinModules)
    }

    @Test
    fun `all ViewModels can be created`() {
        // Test each ViewModel can be injected
        val homeViewModel: HomeViewModel by inject()
        assertNotNull(homeViewModel)

        val sessionViewModel: SessionViewModel by inject()
        assertNotNull(sessionViewModel)

        // ... etc for all ViewModels
    }

    @Test
    fun `all repositories can be created`() {
        val usersRepo: UsersRepository by inject()
        assertNotNull(usersRepo)

        val sessionsRepo: SessionsRepository by inject()
        assertNotNull(sessionsRepo)

        // ... etc
    }

    @Test
    fun `all qualified dispatchers exist`() {
        val ioDispatcher: CoroutineDispatcher by inject(named("IoDispatcher"))
        assertNotNull(ioDispatcher)
        assertEquals(Dispatchers.IO, ioDispatcher)

        // Test all named qualifiers
    }
}
```

### Pros
- ✅ Tests real Android environment
- ✅ Catches Android-specific injection failures
- ✅ Validates qualified dependencies
- ✅ Can verify ViewModel scoping
- ✅ Integration-level confidence

### Cons
- ❌ Requires emulator/device (slower)
- ❌ More setup than unit tests
- ❌ Doesn't run by default in CI (needs instrumentation setup)

### Metrics
- **Complexity:** ⭐⭐ Medium (50-100 lines)
- **Safety Improvement:** ⭐⭐⭐⭐ High (catches 95% of issues)
- **Dev Impact:** ⭐ Minimal (only runs when explicitly triggered)
- **Execution Time:** ~2-5 seconds
- **False Positives:** Very Low

### Recommendation
**✅ RECOMMENDED - High value for effort**

---

## Option 3: Koin Strict Mode ⭐ PRIORITY 3

### Description
Enable Koin's strict mode to fail on certain conditions (like injecting nullable types).

### Implementation
```kotlin
// SimpleHIITApplication.kt
startKoin {
    androidLogger(Level.ERROR)
    androidContext(this@SimpleHIITApplication)

    // Enable strict mode
    allowOverride(false)  // Prevent accidental definition overrides

    modules(allKoinModules)
}
```

### Advanced: Strict Check in Debug
```kotlin
if (BuildConfig.DEBUG) {
    startKoin {
        androidLogger(Level.DEBUG)
        androidContext(this@SimpleHIITApplication)
        allowOverride(false)
        modules(allKoinModules)

        // Verify configuration in debug builds
        checkModules {
            withInstance(this@SimpleHIITApplication)
        }
    }
} else {
    // Production: lighter setup
    startKoin {
        androidLogger(Level.ERROR)
        androidContext(this@SimpleHIITApplication)
        modules(allKoinModules)
    }
}
```

### Pros
- ✅ Catches errors at app startup in debug builds
- ✅ Zero ongoing maintenance
- ✅ Prevents definition overrides (caught the TimerDispatcher issue)
- ✅ Runs automatically during development

### Cons
- ❌ Only catches errors when app actually starts
- ❌ checkModules() at startup adds 100-200ms delay in debug
- ❌ May need to mock dependencies

### Metrics
- **Complexity:** ⭐ Low (5-10 lines)
- **Safety Improvement:** ⭐⭐⭐ Medium (catches issues during manual testing)
- **Dev Impact:** ⭐⭐ Low (slight startup delay in debug)
- **Execution Time:** +100-200ms on app launch (debug only)
- **False Positives:** Low

### Recommendation
**✅ RECOMMENDED - Simple with good ROI**

---

## Option 4: CI Pipeline - Dedicated Verification Job ⭐ PRIORITY 1

### Description
Add a dedicated CI job that runs Koin verification tests.

### Implementation
```yaml
# .github/workflows/koin-verification.yml
name: Koin DI Verification

on:
  pull_request:
    branches: [ main, develop ]
  push:
    branches: [ main, develop ]

jobs:
  verify-koin-modules:
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v4

    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'

    - name: Verify Mobile Koin Configuration
      run: ./gradlew :android:mobile:app:testDebugUnitTest --tests "*KoinConfigurationTest"

    - name: Verify TV Koin Configuration
      run: ./gradlew :android:tv:app:testDebugUnitTest --tests "*KoinConfigurationTest"

    - name: Report
      if: failure()
      run: |
        echo "::error::Koin DI configuration is invalid! Check module definitions."
        exit 1
```

**Alternative: Add to existing CI workflow**
```yaml
# In existing .github/workflows/build.yml
- name: Run Unit Tests (including Koin verification)
  run: ./gradlew testDebugUnitTest
```

### Pros
- ✅ Blocks PRs with DI errors
- ✅ Zero day-to-day impact
- ✅ Runs on every commit
- ✅ Visual feedback in PR checks
- ✅ Prevents broken code from merging

### Cons
- ❌ Only catches errors after commit
- ❌ Adds CI time (minimal ~30s)

### Metrics
- **Complexity:** ⭐ Low (YAML file or add to existing)
- **Safety Improvement:** ⭐⭐⭐⭐ High (mandatory gate)
- **Dev Impact:** ⭐ None (runs in background)
- **Execution Time:** +30 seconds to CI
- **False Positives:** None (same as tests)

### Recommendation
**✅ MUST HAVE - Essential safety net**

---

## Option 5: Git Pre-commit Hook ⚠️ OPTIONAL

### Description
Run Koin verification locally before allowing commits.

### Implementation
```bash
# .git/hooks/pre-commit (or use hooks framework like Husky)
#!/bin/bash

echo "Running Koin DI verification..."

# Run Koin configuration tests
./gradlew :android:mobile:app:testDebugUnitTest --tests "*KoinConfigurationTest" -q

if [ $? -ne 0 ]; then
    echo "❌ Koin DI configuration is invalid!"
    echo "Fix module definitions before committing."
    exit 1
fi

echo "✅ Koin DI verification passed"
exit 0
```

**Better: Use existing Git hooks setup**
```bash
# scripts/git-hooks/pre-commit
# Add Koin check to existing hook if you have one
```

### Pros
- ✅ Catches errors before commit
- ✅ Immediate feedback
- ✅ Prevents broken commits

### Cons
- ❌ Slows down commits (~2-5 seconds)
- ❌ Can be skipped with `--no-verify`
- ❌ Requires hook installation/management
- ❌ Not enforced across team (unless automated setup)
- ❌ Frustrating during rapid iteration

### Metrics
- **Complexity:** ⭐⭐ Medium (hook management)
- **Safety Improvement:** ⭐⭐⭐ Medium (helps but bypassable)
- **Dev Impact:** ⭐⭐⭐ Medium (adds friction to workflow)
- **Execution Time:** +2-5 seconds per commit
- **False Positives:** None

### Recommendation
**⚠️ OPTIONAL - Consider if team wants it**

Note: Your GITHOOKS.md already documents hook setup, so this could integrate well.

---

## Option 6: Custom Static Analysis / DSL Validator ❌ NOT RECOMMENDED

### Description
Build custom static analysis to validate Koin module syntax.

### Example Concept
- Parse Kotlin AST looking for Koin module definitions
- Validate `get()` calls match available definitions
- Check qualifier names exist

### Pros
- ✅ Could catch errors at build time

### Cons
- ❌ **Extremely complex** to implement correctly
- ❌ High maintenance burden
- ❌ Fragile (breaks with Koin updates)
- ❌ Reinventing the wheel
- ❌ Other options provide better ROI

### Metrics
- **Complexity:** ⭐⭐⭐⭐ Very High (weeks of work)
- **Safety Improvement:** ⭐⭐ Low (limited scope)
- **Dev Impact:** ⭐ Minimal (if it works)
- **Maintenance:** High

### Recommendation
**❌ NOT WORTH IT - Use Koin's built-in tools instead**

---

## Option 7: Gradle Task - Dedicated Verification ⚠️ NICE TO HAVE

### Description
Create a custom Gradle task for running all Koin verification tests.

### Implementation
```kotlin
// build.gradle.kts (root)
tasks.register("verifyKoinModules") {
    group = "verification"
    description = "Verifies all Koin DI module configurations are valid"

    dependsOn(
        ":android:mobile:app:testDebugUnitTest",
        ":android:tv:app:testDebugUnitTest"
    )

    doLast {
        println("✅ All Koin module configurations verified successfully")
    }
}

// Make 'check' task depend on it
tasks.named("check") {
    dependsOn("verifyKoinModules")
}
```

### Pros
- ✅ Easy to run locally: `./gradlew verifyKoinModules`
- ✅ Self-documenting task name
- ✅ Can aggregate multiple checks
- ✅ Integrates with existing Gradle workflow

### Cons
- ❌ Doesn't add safety beyond running tests
- ❌ Just a convenience wrapper

### Metrics
- **Complexity:** ⭐⭐ Medium (Gradle task knowledge)
- **Safety Improvement:** ⭐⭐⭐ Medium (makes verification discoverable)
- **Dev Impact:** ⭐ Minimal
- **Execution Time:** Same as underlying tests

### Recommendation
**⚠️ NICE TO HAVE - Good DX but not essential**

---

## Option 8: ViewModel Scope Verification Tests ⭐ PRIORITY 2

### Description
Specifically test ViewModel-scoped dependencies to catch factory/single mismatches.

### Implementation
```kotlin
class ViewModelScopeTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(allKoinModules)
    }

    @Test
    fun `ViewStateMappers are factory scoped`() {
        // Get mapper twice, should be different instances
        val mapper1: HomeViewStateMapper = get()
        val mapper2: HomeViewStateMapper = get()

        assertNotSame(mapper1, mapper2, "ViewStateMappers should be factory-scoped")
    }

    @Test
    fun `Repositories are singleton scoped`() {
        val repo1: UsersRepository = get()
        val repo2: UsersRepository = get()

        assertSame(repo1, repo2, "Repositories should be singleton-scoped")
    }

    @Test
    fun `SoundPoolFactory is singleton scoped`() {
        val factory1: SoundPoolFactory = get()
        val factory2: SoundPoolFactory = get()

        assertSame(factory1, factory2, "SoundPoolFactory should be singleton")
    }
}
```

### Pros
- ✅ Validates scope decisions
- ✅ Prevents performance issues (wrong scope)
- ✅ Documents intended behavior
- ✅ Catches regressions

### Cons
- ❌ Requires maintaining list of expectations
- ❌ More tests to write

### Metrics
- **Complexity:** ⭐⭐ Medium (test per scope category)
- **Safety Improvement:** ⭐⭐⭐⭐ High (prevents subtle bugs)
- **Dev Impact:** ⭐⭐ Low (only runs in test suite)
- **Execution Time:** ~50-100ms
- **False Positives:** None

### Recommendation
**✅ RECOMMENDED - Prevents scope-related bugs**

---

## Recommended Implementation Strategy

### Phase 1: Essential Safety Net (Week 1)
1. ✅ **Option 1:** Add `checkModules()` unit tests
   - `MobileKoinConfigurationTest`
   - `TvKoinConfigurationTest`
2. ✅ **Option 4:** Add CI verification job
   - Integrate with existing workflows

**Expected Impact:** Catch 90% of DI errors before merge

### Phase 2: Enhanced Coverage (Week 2)
3. ✅ **Option 3:** Enable Koin strict mode in debug builds
4. ✅ **Option 2:** Add instrumented DI tests
5. ✅ **Option 8:** Add ViewModel scope tests

**Expected Impact:** Catch 95%+ of DI errors, including subtle ones

### Phase 3: Developer Experience (Optional)
6. ⚠️ **Option 7:** Create `verifyKoinModules` Gradle task
7. ⚠️ **Option 5:** Consider pre-commit hook (team decision)

---

## Cost-Benefit Summary

### High ROI (Implement First):
- **Unit Test checkModules()** - 15 min setup, catches 90% of issues
- **CI Integration** - 10 min setup, prevents broken merges
- **Strict Mode** - 5 min setup, catches issues during dev

### Medium ROI (Implement Second):
- **Instrumented Tests** - 1 hour setup, catches Android-specific issues
- **Scope Verification Tests** - 30 min setup, prevents subtle bugs

### Low ROI (Nice to Have):
- **Gradle Task** - 30 min setup, improves discoverability
- **Git Hooks** - Variable (depends on team preference)

### Negative ROI (Skip):
- **Custom Static Analysis** - Weeks of work, minimal benefit

---

## Comparison to Hilt

| Aspect | Hilt | Koin + Verification Suite |
|--------|------|---------------------------|
| Error Detection | Compile-time | Test-time (sub-second) |
| Build Speed | Slower (KSP processing) | Faster |
| Error Messages | Sometimes cryptic | Clear and specific |
| Configuration Errors | Caught at build | Caught in tests/CI |
| Runtime Safety | High | High (with test suite) |
| Development Flow | Slower builds | Fast builds, fast tests |

**Net Result:** With proper verification suite, Koin provides comparable safety to Hilt with better build performance.

---

## Next Steps

1. Review and approve this strategy
2. Prioritize which options to implement
3. Create implementation tickets
4. Begin with Phase 1 (essential safety net)
