# Hilt to Koin Migration - Progress Checklist

## Migration Strategy: Gradual (Dual DI System)

**Approach:** Maintain both Hilt and Koin during migration, remove Hilt incrementally.

---

## ✅ COMPLETED MODULES (19/19)

### 1. ✅ data module
- [x] Updated `build.gradle.kts` - added both `simplehiit.hilt` and `simplehiit.koin`
- [x] Created `DataModule.kt` (Koin) - repositories
- [x] Created `LocalDataModule.kt` (Koin) - database, datastore
- [x] Created `dispatcherModule` (Koin) - IoDispatcher
- [x] Created `DataModuleHilt.kt` (Hilt temporary)
- [x] Created `LocalDataModuleHilt.kt` (Hilt temporary)
- [x] Removed `@Inject` from `UserMapper`, `SessionMapper`, `SimpleHiitDataStoreManagerImpl` (use @Provides)
- [x] Kept `@Inject` on repositories (use @Binds)

**Files to delete after full migration:**
- `data/src/main/java/fr/shiningcat/simplehiit/data/di/DataModuleHilt.kt`
- `data/src/main/java/fr/shiningcat/simplehiit/data/local/di/LocalDataModuleHilt.kt`
- `data/src/main/java/fr/shiningcat/simplehiit/data/di/IoDispatcher.kt` (qualifier annotation)

### 2. ✅ commonUtils module
- [x] Updated `build.gradle.kts` - added both plugins
- [x] Created `UtilsModuleKoin.kt` (Koin) - utilsModule, dispatchersModule
- [x] Created `UtilsModuleHilt.kt` (Hilt temporary)
- [x] Deleted original `UtilsModule.kt`

**Files to delete after full migration:**
- `commonUtils/src/main/java/fr/shiningcat/simplehiit/commonutils/di/UtilsModuleHilt.kt`
- Qualifier annotations: `@DefaultDispatcher`, `@MainDispatcher`

### 3. ✅ commonResources module
- [x] Analyzed - NO DI NEEDED
- [x] Contains only resources and stateless mappers
- [x] No migration required

---

## ⬜ REMAINING MODULES (14/17)

### Priority 1: Domain Modules (Must Come Before shared-ui)

#### 4. ✅ domain/common
- [x] Updated `build.gradle.kts` - added both `simplehiit.hilt` and `simplehiit.koin`
- [x] Created `CommonDomainModuleKoin.kt` (Koin) - TimerDispatcher, 2 UseCases
- [x] Renamed existing to `CommonDomainModuleHilt.kt` (Hilt temporary)

**Files to delete after full migration:**
- `domain/common/src/main/java/fr/shiningcat/simplehiit/domain/common/di/CommonDomainModuleHilt.kt`
- Qualifier annotations: `@TimerDispatcher`, `@DigitsFormat`, `@ShortFormat`

#### 5. ✅ domain/home
- [x] Updated `build.gradle.kts` - added both plugins
- [x] Created `HomeDomainModule.kt` (Koin) - 5 UseCases
- [x] All UseCases keep `@Inject` (no Hilt module existed)

#### 6. ✅ domain/session
- [x] Updated `build.gradle.kts` - added both plugins
- [x] Created `SessionDomainModule.kt` (Koin) - 5 UseCases
- [x] All UseCases keep `@Inject`

#### 7. ✅ domain/settings
- [x] Updated `build.gradle.kts` - added both plugins
- [x] Created `SettingsDomainModule.kt` (Koin) - 21 UseCases
- [x] All UseCases keep `@Inject`

#### 8. ✅ domain/statistics
- [x] Updated `build.gradle.kts` - added both plugins
- [x] Created `StatisticsDomainModule.kt` (Koin) - 7 UseCases
- [x] All UseCases keep `@Inject`

---

### Priority 2: Shared-UI Modules (After Domain)

#### 9. ✅ shared-ui/home
- [x] Updated `build.gradle.kts` - added both plugins
- [x] Created `HomeModuleKoin.kt` (Koin) - ViewModel, Interactor, ViewStateMapper
- [x] Renamed existing to `HomeModuleHilt.kt` (Hilt temporary)
- [x] Used `factory` for ViewModelComponent-scoped dependencies

**Files to delete after full migration:**
- `shared-ui/home/src/main/java/fr/shiningcat/simplehiit/sharedui/home/di/HomeModuleHilt.kt`

#### 10. ✅ shared-ui/session
- [x] Updated `build.gradle.kts` - added both plugins
- [x] Created `SessionModuleKoin.kt` (Koin) - ViewModel, Interactor, ViewStateMapper, SoundPool
- [x] Renamed existing to `SessionModuleHilt.kt` (Hilt temporary)
- [x] SoundPool as factory (was ViewModelScoped, released in ViewModel.onCleared())

**Files to delete after full migration:**
- `shared-ui/session/src/main/java/fr/shiningcat/simplehiit/sharedui/session/di/SessionModuleHilt.kt`

#### 11. ✅ shared-ui/settings
- [x] Updated `build.gradle.kts` - added both plugins
- [x] Created `SettingsModuleKoin.kt` (Koin) - ViewModel, Interactor, ViewStateMapper
- [x] Renamed existing to `SettingsModuleHilt.kt` (Hilt temporary)

**Files to delete after full migration:**
- `shared-ui/settings/src/main/java/fr/shiningcat/simplehiit/sharedui/settings/di/SettingsModuleHilt.kt`

#### 12. ✅ shared-ui/statistics
- [x] Updated `build.gradle.kts` - added both plugins
- [x] Created `StatisticsModuleKoin.kt` (Koin) - ViewModel, Interactor, ViewStateMapper
- [x] Renamed existing to `StatisticsModuleHilt.kt` (Hilt temporary)

**Files to delete after full migration:**
- `shared-ui/statistics/src/main/java/fr/shiningcat/simplehiit/sharedui/statistics/di/StatisticsModuleHilt.kt`

---

### Priority 2.5: Android Common Modules (Before App Modules)

#### 13. ✅ android:common
- [x] Updated `build.gradle.kts` - added both `simplehiit.hilt` and `simplehiit.koin`
- [x] Created `CommonAndroidModuleKoin.kt` (Koin) - NavigationViewModel
- [x] Created `CommonAndroidModuleHilt.kt` (Hilt temporary)
- [x] Removed `@HiltViewModel` and `@Inject` from NavigationViewModel

**Files to delete after full migration:**
- `android/common/src/main/java/fr/shiningcat/simplehiit/android/common/di/CommonAndroidModuleHilt.kt`

#### 14. ✅ testUtils
- [x] Updated `build.gradle.kts` - added both plugins
- [x] Created `KoinTestRunner.kt` (Koin) - KoinTestRunner, KoinTestApplication
- [x] Kept `HiltTestRunner.kt` (Hilt temporary)
- [x] AbstractMockkTest unchanged (no DI usage)

**Files to delete after full migration:**
- `testUtils/src/main/java/fr/shiningcat/simplehiit/testutils/HiltTestRunner.kt`

---

### Priority 3: App Modules (Final Step)

#### 15. ✅ android/mobile/app
- [x] Updated `build.gradle.kts` - added both `simplehiit.hilt` and `simplehiit.koin`
- [x] Created `AppModuleKoin.kt` (Koin) - LocaleManager, AndroidVersionProvider, DurationStringFormatters, MainViewModel
- [x] Created `allKoinModules` list aggregating all app modules
- [x] Created `AppModuleHilt.kt` (Hilt temporary)
- [x] Deleted original `AppModule.kt`
- [x] Updated `SimpleHIITApplication` - initialized Koin with all modules
- [x] Updated `MainActivity` - uses Koin injection (kept Hilt @AndroidEntryPoint temporarily)
- [x] Removed `@HiltViewModel` and `@Inject` from MainViewModel

**Files to delete after full migration:**
- `android/mobile/app/src/main/java/fr/shiningcat/simplehiit/android/mobile/app/di/AppModuleHilt.kt`

#### 16. ✅ android/tv/app
- [x] Updated `build.gradle.kts` - added both `simplehiit.hilt` and `simplehiit.koin`
- [x] Added missing module dependencies (domain.*, sharedUi.*)
- [x] Created `AppModuleKoin.kt` (Koin) - LocaleManager, AndroidVersionProvider, DurationStringFormatters, MainViewModel
- [x] Created `allKoinModules` list aggregating all app modules
- [x] Created `AppModuleHilt.kt` (Hilt temporary)
- [x] Deleted original `AppModule.kt`
- [x] Updated `SimpleHIITApplication` - initialized Koin with all modules
- [x] Updated `MainActivity` - uses Koin injection (kept Hilt @AndroidEntryPoint temporarily)
- [x] Removed `@HiltViewModel` and `@Inject` from MainViewModel

**Files to delete after full migration:**
- `android/tv/app/src/main/java/fr/shiningcat/simplehiit/android/tv/app/di/AppModuleHilt.kt`

#### 17. ✅ android/mobile/ui/* modules
- [x] Analyzed all modules: common, home, session, settings, statistics
- [x] NO DI CODE FOUND - modules contain only UI composables
- [x] No migration required

#### 18. ✅ android/tv/ui/* modules
- [x] Analyzed all modules: common, home, session, settings, statistics
- [x] NO DI CODE FOUND - modules contain only UI composables
- [x] No migration required

---

### Priority 4: clean up

verify all di declarations are the simplest they can be, do they use unnecessary large scopes (like a single for a stateless object)
verify all koin declarations use the shortest and simplest syntax (some di frameworks allow for implicit definitions)

---

## 🔑 KEY LESSONS LEARNED

### 1. **@Binds vs @Provides Conflict**
```kotlin
// ❌ WRONG - Creates conflict
@Inject constructor(...) { }  // In class
@Provides fun provide...() = ClassName()  // In module

// ✅ CORRECT - Choose ONE:
// Option A: @Binds (requires @Inject)
@Inject constructor(...) { }
@Binds fun bind...(impl: Impl): Interface

// Option B: @Provides (NO @Inject)
constructor(...) { }  // Plain constructor
@Provides fun provide...() = ClassName()
```

### 2. **Parameter Names Must Match EXACTLY**
```kotlin
// ❌ WRONG
class HiitLoggerImpl(private val isDebug: Boolean)
HiitLoggerImpl(isDebugBuild = isDebug)  // Parameter name mismatch!

// ✅ CORRECT
class HiitLoggerImpl(private val isDebugBuild: Boolean)
HiitLoggerImpl(isDebugBuild = isDebug)  // Exact match
```

### 3. **Dispatcher Qualifiers: Hilt vs Koin**
```kotlin
// HILT
@IoDispatcher private val ioDispatcher: CoroutineDispatcher

// KOIN
get(named("IoDispatcher"))
```

### 4. **androidContext() in Koin**
```kotlin
// Access Android context in Koin modules
single<HiitLogger> {
    val isDebug = (androidContext().applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    HiitLoggerImpl(isDebugBuild = isDebug)
}
```

### 5. **Dependency Order Matters**
Always migrate bottom-up:
```
app
 ↓
shared-ui (ViewModels, Interactors)
 ↓
domain (UseCases)
 ↓
data (Repositories)
 ↓
commonUtils (Logger, TimeProvider, etc.)
```

---

## 📋 STEP-BY-STEP MIGRATION TEMPLATE

For each module:

### 1. Update build.gradle.kts
```kotlin
plugins {
    alias(libs.plugins.simplehiit.hilt)  // Keep temporarily
    alias(libs.plugins.simplehiit.koin)  // Add
}
```

### 2. Create Koin Module
```kotlin
// Example: domain/common/di/CommonDomainModuleKoin.kt
package ...di

import org.koin.core.qualifier.named
import org.koin.dsl.module

val commonDomainModule = module {
    // UseCases
    single { ResetWholeAppUseCase(get(), get(), get(named("DefaultDispatcher")), get()) }

    // Dispatchers
    single(named("TimerDispatcher")) { Dispatchers.Default }
}
```

### 3. Create Hilt Module (Temporary)
```kotlin
// Example: domain/common/di/CommonDomainModuleHilt.kt
@Module
@InstallIn(SingletonComponent::class)
object CommonDomainModuleHilt {
    // Copy existing Hilt setup
}
```

### 4. Handle @Inject Annotations
- **If using @Binds in Hilt:** KEEP @Inject
- **If using @Provides in Hilt:** REMOVE @Inject

### 5. Delete Original Module
```bash
rm path/to/original/Module.kt
```

### 6. Verify
- Sync Gradle
- Check for compilation errors
- Pay attention to parameter names!

---

## 🎯 NEXT SESSION GOALS

1. **Migrate domain/common**
   - Simplest domain module
   - Only 2 UseCases + dispatcher

2. **Migrate domain/home**
   - More UseCases to practice with

3. **If time permits:** domain/session, domain/settings, domain/statistics

---

## ⚠️ COMMON PITFALLS

1. **Parameter name mismatches** - Always double-check constructor parameter names!
2. **Missing named qualifiers** - Remember `named("IoDispatcher")` not just `get()`
3. **Wrong import** - `import org.koin.dsl.module` not `dagger.Module`
4. **Forgetting androidContext()** - Use `androidContext()` to access Android Context
5. **@Inject conflicts** - Classes with @Provides must NOT have @Inject

---

## 📊 PROGRESS TRACKER

```
Total Modules: 19
Completed: 19 (100%) 🎉
Remaining: 0 (0%)

✅ data
✅ commonUtils
✅ commonResources (no DI)
✅ domain/common
✅ domain/home
✅ domain/session
✅ domain/settings
✅ domain/statistics
✅ shared-ui/home
✅ shared-ui/session
✅ shared-ui/settings
✅ shared-ui/statistics
✅ android:common
✅ testUtils
✅ android/mobile/app
✅ android/tv/app
✅ android/mobile/ui/* (no DI)
✅ android/tv/ui/* (no DI)
```

---

## 🧹 FINAL CLEANUP (After All Migrations)

Once ALL modules are migrated to Koin:

### 1. Remove Hilt Plugin from All Modules
```kotlin
// Remove from all build.gradle.kts:
alias(libs.plugins.simplehiit.hilt)
```

### 2. Delete All Hilt Modules
```bash
# Find and delete
find . -name "*ModuleHilt.kt" -delete
```

### 3. Delete Qualifier Annotations
```bash
# Delete files like:
# data/src/.../di/IoDispatcher.kt
# commonUtils/src/.../di/DefaultDispatcher.kt
# domain/common/src/.../di/TimerDispatcher.kt
```

### 4. Remove @Inject Annotations
- Remove from all classes
- Remove import `javax.inject.Inject`

### 5. Remove Hilt Dependencies
Update `build-logic/convention` to remove Hilt setup

---

**Document Created:** 2025-12-29
**Last Updated:** 2025-12-29 18:07 CET
**Status:** 🎉 MIGRATION COMPLETE! All 19 modules migrated to Koin (100%). Ready for Hilt cleanup phase.
