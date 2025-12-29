# Hilt to Koin Migration Plan

## Executive Summary

This document outlines the migration strategy from Hilt to Koin for dependency injection in the SimpleHIIT Android application. The project currently uses Hilt 2.57.2 across 26 modules with extensive DI coverage including:

- 2 Application classes (@HiltAndroidApp)
- 2 MainActivity classes (@AndroidEntryPoint)
- Multiple ViewModels (@HiltViewModel)
- 124+ injection points (@Inject)
- Multiple DI modules with custom qualifiers
- ViewModelComponent and SingletonComponent scopes

## Current State Analysis

### Hilt Dependencies
```toml
hilt = "2.57.2"
composeNavigationHilt = "1.3.0"
```

### Convention Plugin
- `HiltConventionPlugin.kt` applies KSP and configures Hilt dependencies
- Applied to 26 modules via `alias(libs.plugins.simplehiit.hilt)`

### Injection Scope Analysis

**Application Scope (SingletonComponent):**
- Data repositories (Users, Sessions, Settings, Language)
- Room database and DAOs
- Coroutine dispatchers with qualifiers (@IoDispatcher, @TimerDispatcher, @MainDispatcher, @DefaultDispatcher)
- AndroidVersionProvider
- LocaleManager
- DurationStringFormatter with qualifiers (@DigitsFormat, @ShortFormat)
- TimeProvider
- HiitLogger

**ViewModel Scope (ViewModelComponent):**
- UI Interactors (Home, Session, Settings, Statistics)
- SoundPool (session-specific)
- ViewStateMappers

**ViewModels:**
- HomeViewModel
- SessionViewModel
- SettingsViewModel
- StatisticsViewModel
- NavigationViewModel
- MainViewModel (mobile + TV)

### Custom Qualifiers in Use
- `@IoDispatcher` - IO thread dispatcher
- `@TimerDispatcher` - Timer thread dispatcher
- `@MainDispatcher` - Main thread dispatcher
- `@DefaultDispatcher` - Default thread dispatcher
- `@DigitsFormat` - Duration formatter (digits format)
- `@ShortFormat` - Duration formatter (short format)

## Migration Strategy

### Phased Approach

**Phase 1: Setup & Infrastructure (Estimated: 2-3 hours)**
1. Add Koin dependencies to version catalog
2. Create KoinConventionPlugin to replace HiltConventionPlugin
3. Update build configuration files
4. Remove Hilt-specific Kover exclusions

**Phase 2: Core Module Migration (Estimated: 4-6 hours)**
1. Migrate data layer modules (repositories, DAOs, dispatchers)
2. Migrate commonUtils module (logger, time provider, dispatchers)
3. Create Koin modules for application-scoped dependencies
4. Test data layer in isolation

**Phase 3: Domain Layer Migration (Estimated: 3-4 hours)**
1. Migrate domain use cases across all feature modules
2. Convert @Inject constructors to regular constructors
3. Update domain module Koin definitions
4. Test domain layer

**Phase 4: Shared-UI Layer Migration (Estimated: 4-6 hours)**
1. Migrate ViewStateMappers
2. Migrate Interactors
3. Convert ViewModels from @HiltViewModel to Koin viewModel
4. Update ViewModel modules for each feature

**Phase 5: Application & Activity Migration (Estimated: 2-3 hours)**
1. Convert Application classes from @HiltAndroidApp to Koin initialization
2. Convert MainActivity classes from @AndroidEntryPoint to Koin integration
3. Replace androidx.hilt.navigation.compose with Koin navigation
4. Update app-specific modules (LocaleManager, formatters)

**Phase 6: Testing & Cleanup (Estimated: 3-4 hours)**
1. Update test infrastructure
2. Migrate Hilt test utilities to Koin test utilities
3. Remove all Hilt dependencies
4. Remove HiltConventionPlugin
5. Update documentation
6. Final integration testing

### Total Estimated Time: 18-26 hours

## Detailed Implementation Plan

### 1. Dependency Updates

**Add to `gradle/libs.versions.toml`:**
```toml
[versions]
koin = "4.0.0"
koinCompose = "4.0.0"
koinAndroidxCompose = "4.0.0"
koinTest = "4.0.0"

[libraries]
koin-core = { module = "io.insert-koin:koin-core", version.ref = "koin" }
koin-android = { module = "io.insert-koin:koin-android", version.ref = "koin" }
koin-androidx-compose = { module = "io.insert-koin:koin-androidx-compose", version.ref = "koinAndroidxCompose" }
koin-androidx-compose-navigation = { module = "io.insert-koin:koin-androidx-compose-navigation", version.ref = "koinAndroidxCompose" }
koin-test = { module = "io.insert-koin:koin-test", version.ref = "koinTest" }
koin-test-junit = { module = "io.insert-koin:koin-test-junit4", version.ref = "koinTest" }

[plugins]
# Remove or deprecate:
# hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }

# Add:
simplehiit-koin = { id = "fr.shiningcat.simplehiit.koin" }
```

**Remove from version catalog:**
- All hilt-* libraries
- composeNavigationHilt
- ksp plugin (if only used for Hilt)

### 2. Create KoinConventionPlugin

**File: `build-logic/convention/src/main/kotlin/fr/shiningcat/simplehiit/plugins/KoinConventionPlugin.kt`**

```kotlin
package fr.shiningcat.simplehiit.plugins

import fr.shiningcat.simplehiit.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KoinConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            dependencies {
                add("implementation", libs.findLibrary("koin.core").get())
                add("implementation", libs.findLibrary("koin.android").get())
                add("testImplementation", libs.findLibrary("koin.test").get())
                add("testImplementation", libs.findLibrary("koin.test.junit").get())
            }
        }
    }
}
```

### 3. Module Migration Patterns

#### Pattern A: @Binds Interface → Koin single/factory

**Before (Hilt):**
```kotlin
@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Singleton
    @Binds
    fun bindsUsersRepository(usersRepository: UsersRepositoryImpl): UsersRepository
}
```

**After (Koin):**
```kotlin
val dataModule = module {
    single<UsersRepository> { UsersRepositoryImpl(get(), get(), get()) }
}
```

#### Pattern B: @Provides Object → Koin single/factory

**Before (Hilt):**
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DataDispatcherModule {
    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
```

**After (Koin):**
```kotlin
val dispatcherModule = module {
    single(qualifier = named("IoDispatcher")) { Dispatchers.IO }
}
```

#### Pattern C: ViewModels

**Before (Hilt):**
```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeInteractor: HomeInteractor,
    private val homeViewStateMapper: HomeViewStateMapper,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val hiitLogger: HiitLogger,
) : ViewModel()
```

**After (Koin):**
```kotlin
class HomeViewModel(
    private val homeInteractor: HomeInteractor,
    private val homeViewStateMapper: HomeViewStateMapper,
    private val mainDispatcher: CoroutineDispatcher,
    private val hiitLogger: HiitLogger,
) : ViewModel()

// In module:
val homeModule = module {
    viewModel {
        HomeViewModel(
            get(),
            get(),
            get(named("MainDispatcher")),
            get()
        )
    }
}
```

#### Pattern D: Application Class

**Before (Hilt):**
```kotlin
@HiltAndroidApp
class SimpleHIITApplication : Application()
```

**After (Koin):**
```kotlin
class SimpleHIITApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@SimpleHIITApplication)
            modules(
                appModule,
                dataModule,
                domainModule,
                uiModule,
                // ... all modules
            )
        }
    }
}
```

#### Pattern E: Activity

**Before (Hilt):**
```kotlin
@AndroidEntryPoint
class MainActivity : ComponentActivity()
```

**After (Koin):**
```kotlin
class MainActivity : ComponentActivity() {
    // No annotation needed - Koin works automatically
}
```

#### Pattern F: Composable ViewModel Injection

**Before (Hilt):**
```kotlin
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel())
```

**After (Koin):**
```kotlin
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel())
```

### 4. Qualifier Migration

Custom qualifiers in Hilt become named qualifiers in Koin:

**Before:**
```kotlin
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher
```

**After:**
```kotlin
// Use Koin's built-in named() function
val name = named("IoDispatcher")
```

### 5. Module Organization

Recommended Koin module structure:

```
android/mobile/app/di/
  ├── AppModule.kt (application-level dependencies)
  └── KoinModules.kt (aggregates all modules)

android/tv/app/di/
  ├── AppModule.kt
  └── KoinModules.kt

data/di/
  ├── DataModule.kt
  ├── DispatcherModule.kt
  └── LocalDataModule.kt

domain/*/di/
  └── DomainModule.kt (per feature)

shared-ui/*/di/
  └── UiModule.kt (per feature)

commonUtils/di/
  └── UtilsModule.kt
```

### 6. Testing Migration

**Before (Hilt):**
```kotlin
@HiltAndroidTest
class MyTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)
}
```

**After (Koin):**
```kotlin
class MyTest : KoinTest {
    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(testModule)
    }
}
```

## Risk Assessment & Mitigation

### High Risk Areas

1. **Circular Dependencies**
   - **Risk:** Koin detects circular dependencies at runtime, Hilt catches them at compile time during code generation
   - **Important Distinction:** This is NOT the same as your module dependency graph checks (modules-graph-assert plugin)
     - Module graph checks: Prevent `moduleA` depending on `moduleB` when it shouldn't (architectural boundaries)
     - DI circular dependencies: Prevent `ClassA(classB: ClassB)` and `ClassB(classA: ClassA)` (object instantiation)
   - **Why This Matters:**
     - With Hilt, if you accidentally create a circular dependency between classes, KSP fails during compilation with a clear error
     - With Koin, the app compiles fine, but crashes at runtime when trying to instantiate the dependency graph
     - Example scenario:
       ```kotlin
       class SessionInteractor @Inject constructor(
           private val buildSessionUseCase: BuildSessionUseCase
       )

       class BuildSessionUseCase @Inject constructor(
           private val sessionInteractor: SessionInteractor  // CIRCULAR!
       )
       ```
     - Hilt: Build fails with "cycle detected" error
     - Koin: App builds, but crashes on launch when Koin tries to create the graph
   - **Mitigation:**
     - Add comprehensive Koin initialization tests in CI that start the DI graph
     - Create a dedicated test that validates all module definitions load without errors
     - Example test:
       ```kotlin
       @Test
       fun `verify Koin configuration is valid`() {
           startKoin {
               modules(allModules)
           }.checkModules()
       }
       ```
     - Use lazy injection (`inject()` instead of constructor parameters) only where truly needed
     - Given your clean architecture, risk is relatively low - your existing module boundaries naturally prevent circular dependencies

2. **ViewModelComponent Scoped Dependencies**
   - **Risk:** Hilt's `ViewModelComponent` scope has no direct equivalent in Koin, affecting lifecycle management
   - **Current Implementation Analysis:**
     ```kotlin
     // In SessionModule.kt - scoped to ViewModelComponent
     @Provides
     @ViewModelScoped
     fun provideSoundPool(): SoundPool = SoundPool.Builder()...
     ```
   - **What ViewModelComponent Scope Means:**
     - In Hilt: SoundPool instance is created when SessionViewModel is created
     - Same instance is injected into SessionViewModel and SessionInteractor
     - SoundPool is automatically destroyed when SessionViewModel is cleared
     - Survives configuration changes (rotation) because ViewModel survives
     - Gets recreated if you navigate away and back to the session screen
   - **Koin's ViewModel Scoping:**
     - `viewModel { }` - Creates a ViewModel instance (Koin knows about ViewModel lifecycle)
     - `viewModelScope { }` - Creates an object tied to a specific ViewModel instance (NOT widely used)
     - `factory { }` - Creates a new instance every time it's requested
     - `single { }` - Creates one instance for the entire app lifecycle
   - **The Problem:**
     - If we use `factory` for SoundPool: New instance created every time, potential resource leaks
     - If we use `single` for SoundPool: Lives for entire app lifecycle, wastes resources when not on session screen
     - `viewModelScope` exists but is awkward - requires passing ViewModel as parameter
   - **Testability vs Lifecycle Trade-offs:**

     You're absolutely right that direct instantiation makes testing harder. Looking at your current implementation:
     - SoundPool is injected into SessionViewModel
     - Complex initialization (load complete listener)
     - Exposed through `getSoundPool()` method
     - Used in SessionInteractor as well

     **The Testability Problem:**
     ```kotlin
     // If we do this:
     class SessionViewModel(...) {
         private val soundPool = SoundPool.Builder()...  // Hard to mock!
     }

     // Tests become difficult:
     @Test
     fun `test beep sound plays on countdown`() {
         // How do we verify soundPool.play() was called?
         // How do we control onLoadComplete callback timing?
     }
     ```

     **Recommended Solutions (maintaining testability):**

     1. **Factory Pattern with Proper Lifecycle (Recommended):**
        ```kotlin
        // Define a factory interface for testability
        interface SoundPoolFactory {
            fun create(): SoundPool
        }

        class SoundPoolFactoryImpl : SoundPoolFactory {
            override fun create(): SoundPool =
                SoundPool.Builder()
                    .setMaxStreams(1)
                    .setAudioAttributes(...)
                    .build()
        }

        // In Koin module
        val sessionModule = module {
            single<SoundPoolFactory> { SoundPoolFactoryImpl() }
            viewModel { SessionViewModel(get(), get(), get(), get(), get()) }
        }

        // In SessionViewModel
        class SessionViewModel(
            private val sessionInteractor: SessionInteractor,
            private val mapper: SessionViewStateMapper,
            private val mainDispatcher: CoroutineDispatcher,
            private val timeProvider: TimeProvider,
            private val soundPoolFactory: SoundPoolFactory,
            private val hiitLogger: HiitLogger,
        ) : ViewModel() {
            private val soundPool: SoundPool by lazy { soundPoolFactory.create() }

            override fun onCleared() {
                if (::soundPool.isInitialized) {
                    soundPool.release()
                }
                super.onCleared()
            }
        }

        // Testing becomes easy:
        @Test
        fun `test beep sound lifecycle`() {
            val mockSoundPool = mockk<SoundPool>(relaxed = true)
            val mockFactory = mockk<SoundPoolFactory> {
                every { create() } returns mockSoundPool
            }

            val viewModel = SessionViewModel(..., mockFactory, ...)
            // Trigger sound load
            verify { mockSoundPool.setOnLoadCompleteListener(any()) }
        }
        ```

     2. **Koin Scopes (More Complex but Powerful):**
        ```kotlin
        // Define a custom scope for session lifecycle
        val sessionModule = module {
            scope<SessionViewModel> {
                scoped {
                    SoundPool.Builder()
                        .setMaxStreams(1)
                        .setAudioAttributes(...)
                        .build()
                }
            }
            viewModel { SessionViewModel(get(), get(), get(), get(), getScope()) }
        }

        // In SessionViewModel
        class SessionViewModel(
            ...,
            private val sessionScope: Scope
        ) : ViewModel() {
            private val soundPool: SoundPool by sessionScope.inject()

            override fun onCleared() {
                soundPool.release()
                sessionScope.close()  // Clean up scope
                super.onCleared()
            }
        }

        // Testing:
        @Test
        fun test() {
            val testScope = koin.createScope<SessionViewModel>()
            testScope.declare { mockk<SoundPool>(relaxed = true) }
            val viewModel = SessionViewModel(..., testScope)
        }
        ```

     3. **Wrapper Abstraction (Alternative):**
        ```kotlin
        // Wrap SoundPool in a testable interface
        interface AudioPlayer {
            fun loadSound(resId: Int, onComplete: (Int, Int) -> Unit)
            fun play(soundId: Int)
            fun release()
        }

        class SoundPoolAudioPlayer(
            context: Context
        ) : AudioPlayer {
            private val soundPool = SoundPool.Builder()...

            override fun loadSound(resId: Int, onComplete: (Int, Int) -> Unit) {
                soundPool.setOnLoadCompleteListener { pool, id, status ->
                    onComplete(id, status)
                }
                soundPool.load(context, resId, 1)
            }
            // ... other methods
        }

        // In Koin - inject the abstraction
        val sessionModule = module {
            factory<AudioPlayer> { SoundPoolAudioPlayer(androidContext()) }
            viewModel { SessionViewModel(..., get<AudioPlayer>()) }
        }

        // Testing - mock the abstraction
        @Test
        fun test() {
            val mockAudioPlayer = mockk<AudioPlayer>(relaxed = true)
            val viewModel = SessionViewModel(..., mockAudioPlayer)
            verify { mockAudioPlayer.play(any()) }
        }
        ```

   - **Recommended Approach:**
     - **Option 1 (Factory Pattern)** is the simplest and most maintainable
     - Keeps your existing architecture mostly intact
     - Easy to test - just mock the factory
     - Clear lifecycle management in ViewModel
     - Minimal changes to existing code structure

   - **Key Principle:**
     > "Inject factories for objects with complex lifecycle management or platform dependencies that need mocking in tests"

   - **Testing Requirements:**
     - Mock SoundPoolFactory in ViewModel tests to verify lifecycle
     - Verify SoundPool is released on ViewModel clear
     - Test configuration changes don't leak instances
     - Integration tests with real SoundPool on device/emulator
     - Monitor memory during navigation (LeakCanary recommended)

3. **ViewModel Scoping & State Management**
   - **Risk:** Koin and Hilt handle ViewModel lifecycle similarly, but subtle differences in how they're retrieved
   - **Hilt Behavior:**
     ```kotlin
     @HiltViewModel
     class HomeViewModel @Inject constructor(...) : ViewModel()

     @Composable
     fun HomeScreen(viewModel: HomeViewModel = hiltViewModel())
     ```
     - ViewModel scoped to ViewModelStoreOwner (Activity/Fragment/NavBackStackEntry)
     - Survives configuration changes automatically
     - Cleared when owner is destroyed
   - **Koin Behavior:**
     ```kotlin
     class HomeViewModel(...) : ViewModel()

     @Composable
     fun HomeScreen(viewModel: HomeViewModel = koinViewModel())
     ```
     - Same scoping behavior - uses ViewModelStore under the hood
     - Should behave identically in practice
   - **Potential Issues:**
     - Navigation: Verify ViewModels are properly scoped to navigation destinations
     - Different `koinViewModel()` overloads for different scopes - ensure correct usage
     - Testing: Mock ViewModels differently in tests
   - **Mitigation:**
     - Test rotation, process death, navigation scenarios thoroughly
     - Verify state preservation across configuration changes
     - Check that ViewModels are cleared when expected (no memory leaks)
     - Existing ViewModel tests should catch most issues

4. **Custom Qualifiers**
   - Risk: Multiple dispatchers and formatters use qualifiers
   - Mitigation: Create comprehensive named qualifier mapping
   - Document all qualifier conversions

### Medium Risk Areas

1. **Navigation Integration**
   - Risk: Replacing androidx.hilt.navigation.compose
   - Mitigation: Test all navigation scenarios
   - Verify ViewModel retention across navigation

2. **Multi-Module Build**
   - Risk: 26 modules need coordinated migration
   - Mitigation: Migrate in dependency order (data → domain → ui → app)
   - Create intermediate branch for rollback capability

3. **Test Infrastructure**
   - Risk: Extensive test suite needs updating
   - Mitigation: Migrate tests alongside production code
   - Maintain test coverage metrics

### Low Risk Areas

1. **Constructor Injection**
   - Risk: Minimal - removing @Inject annotation is straightforward
   - Mitigation: IDE refactoring tools handle this well

2. **Module Definitions**
   - Risk: Low - Koin DSL is more concise than Hilt
   - Mitigation: Follow established patterns

## Verification Checklist

### Per-Module Verification
- [ ] All @Inject annotations removed
- [ ] Module definition created in di/ package
- [ ] Dependencies correctly declared (single/factory/viewModel)
- [ ] Qualifiers migrated to named()
- [ ] Build succeeds
- [ ] Unit tests pass
- [ ] Integration tests pass

### Application-Level Verification
- [ ] Koin initialization in Application class
- [ ] All modules loaded
- [ ] No circular dependency errors
- [ ] App launches successfully
- [ ] All screens accessible
- [ ] ViewModels initialize correctly
- [ ] Navigation works
- [ ] Process death recovery works
- [ ] Configuration changes handled
- [ ] All integration tests pass

### Final Cleanup Verification
- [ ] All Hilt dependencies removed
- [ ] HiltConventionPlugin removed
- [ ] KSP removed (if only used for Hilt)
- [ ] Hilt Kover exclusions removed
- [ ] Build performance benchmarked
- [ ] Documentation updated
- [ ] Migration guide created

## Expected Benefits

1. **Compilation Performance**
   - No KSP annotation processing
   - Faster incremental builds
   - Estimated 20-30% build time improvement

2. **Code Simplicity**
   - Less boilerplate (no @HiltViewModel, @AndroidEntryPoint, etc.)
   - More readable module definitions
   - Simpler testing setup

3. **Runtime Flexibility**
   - Dynamic module loading
   - Easier to mock/override in tests
   - More straightforward debugging

4. **Multiplatform Ready**
   - Koin supports KMP
   - Future-proofs for potential shared code

## Rollback Plan

If critical issues arise during migration:

1. **Branch Strategy**
   - Keep Hilt implementation in main branch
   - Migrate in feature/koin-migration branch
   - Merge only when fully verified

2. **Hybrid Approach** (if needed)
   - Koin and Hilt can coexist temporarily
   - Migrate modules incrementally
   - Maintain both DI systems until complete

3. **Rollback Procedure**
   - Revert to previous commit
   - Restore Hilt dependencies
   - Re-apply HiltConventionPlugin
   - Rebuild all modules

## Timeline Proposal

### Week 1: Foundation
- Days 1-2: Setup Koin dependencies and convention plugin
- Days 3-5: Migrate data layer and test

### Week 2: Domain & Infrastructure
- Days 1-3: Migrate all domain modules
- Days 4-5: Migrate commonUtils and test utilities

### Week 3: UI Layer
- Days 1-3: Migrate shared-ui modules
- Days 4-5: Migrate platform-specific UI modules

### Week 4: Integration & Polish
- Days 1-2: Migrate application classes and final integration
- Days 3-4: Comprehensive testing
- Day 5: Documentation and cleanup

## Open Questions

1. Should we keep KSP for Room, or does removing Hilt eliminate KSP entirely?
   - Answer: Room still uses KSP, so it stays

2. Do we want to use Koin annotations (@Single, @Factory) or pure DSL?
   - Recommendation: Pure DSL for consistency and clarity

3. Should we maintain the same module structure or consolidate?
   - Recommendation: Keep existing structure for easier migration

4. How do we handle the workaround for Kotlin 2.3.0 + Hilt compatibility?
   - Answer: This becomes obsolete with Koin

## References

- [Koin Documentation](https://insert-koin.io/docs/reference/koin-android/start)
- [Koin Compose Integration](https://insert-koin.io/docs/reference/koin-compose/compose)
- [Migrating from Dagger/Hilt to Koin](https://insert-koin.io/docs/reference/koin-android/dagger-migration)
- [Koin Testing](https://insert-koin.io/docs/reference/koin-test/testing)
