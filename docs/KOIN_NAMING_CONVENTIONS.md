<!--
  ~ SPDX-FileCopyrightText: 2024-2026 shining-cat
  ~ SPDX-License-Identifier: GPL-3.0-or-later
  -->
# Koin DI Naming Conventions & Scoping Rules

**Purpose:** Document scoping conventions for Koin dependency injection
**Enforcement:** Code reviews + configuration tests
**Last Updated:** 2025-12-31

---

## Overview

This project uses **naming conventions** to determine the correct Koin scope for dependency injection objects. Naming conventions serve as documentation and guide development.

## Scoping Quick Reference

| Naming Pattern | Koin Scope | When Created | Example |
|----------------|------------|--------------|---------|
| `*Mapper` | `factory` | Per injection | `UserMapper`, `SessionMapper` |
| `*ViewStateMapper` | `factory` | Per injection | `HomeViewStateMapper` |
| `*Interactor` | `factory` | Per injection | `HomeInteractor` |
| `*Logger` | `factory` | Per injection | `HiitLogger` |
| `*UseCase` | `factory` | Per injection | `BuildSessionUseCase` |
| `*ViewModel` | `viewModel` | Per screen | `HomeViewModel` |
| `*Repository` | `single` | Once, app-wide | `UsersRepository` |
| `*Provider` | `single` | Once, app-wide | `TimeProvider` |
| `*Manager` | `single` | Once, app-wide | `LocaleManager` |
| `*Factory` | `single` | Once, app-wide | `SoundPoolFactory` |
| `*Database`, `*Dao` | `single` | Once, app-wide | `SimpleHiitDatabase`, `UsersDao` |
| `*DataStore*` | `single` | Once, app-wide | `SimpleHiitDataStoreManager` |
| `*Formatter` | `single` | Once, app-wide | `DurationStringFormatter` |
| Named `*Dispatcher` | `single` | Once, app-wide | `get(named("IoDispatcher"))` |

---

## Detailed Naming Rules

### 1. `*Mapper` → `factory` Scope

**Pattern:** Any class ending with `Mapper` (except `ViewStateMapper`)

**Scope:** `factory` - New instance per injection

**Rationale:**
- Mappers are **stateless** conversion functions
- No reason to share instances across consumers
- Factory prevents unintended coupling
- Memory cost is negligible (small objects)

**Examples:**
```kotlin
// Correct
factory { UserMapper() }
// Wrong - unnecessary singleton for stateless object
single { UserMapper() }
```

---

### 2. `*ViewStateMapper` → `factory` Scope

**Pattern:** Class ending with `ViewStateMapper`

**Scope:** `factory` - New instance per injection

**Rationale:**
- Converts domain models to UI state
- Stateless transformation logic
- Each ViewModel gets its own instance
- Prevents state leakage between screens

**Examples:**
```kotlin
// CORRECT
factory { HomeViewStateMapper(get(), get()) }
// Wrong - unnecessary singleton for stateless object
single { HomeViewStateMapper(get(), get()) }
```

---

### 3. `*Interactor` → `factory` Scope

**Pattern:** Class ending with `Interactor`

**Scope:** `factory` - New instance per injection

**Rationale:**
- Mediates between ViewModel and UseCases
- Stateless coordination logic
- One per ViewModel (injected into ViewModel constructor)
- Factory ensures clean instances

**Examples:**
```kotlin
// CORRECT
factory<HomeInteractor> {
    HomeInteractorImpl(get(), get(), get(), get(), get())
}
```

---

### 4. `*Repository` → `single` Scope

**Pattern:** Class ending with `Repository`

**Scope:** `single` - One instance app-wide

**Rationale:**
- **Maintains state** (cache, in-memory data)
- Coordinates data sources (local + remote)
- Expensive to create (database, network setup)
- Must be shared to maintain consistency

**Examples:**
```kotlin
// CORRECT
single<UsersRepository> {
    UsersRepositoryImpl(get(), get(), get(named("IoDispatcher")), get())
}

class UsersRepositoryImpl(
    private val usersDao: UsersDao,
    private val userMapper: UserMapper,
    private val ioDispatcher: CoroutineDispatcher,
    private val logger: HiitLogger
) : UsersRepository {
    // Internal cache, database connection, etc.
}
```
---

### 5. `*Provider` → `single` Scope

**Pattern:** Class ending with `Provider`

**Scope:** `single` - One instance app-wide

**Rationale:**
- Wraps system APIs (`System.currentTimeMillis`, `Build.VERSION.SDK_INT`)
- Stateless but no need for multiple instances
- One wrapper per app is sufficient

**Examples:**
```kotlin
// CORRECT
single<TimeProvider> { TimeProviderImpl() }
single<AndroidVersionProvider> { AndroidVersionProviderImpl() }
```

**Could be factory?** Technically yes, but:
- Injected into singletons → only created once anyway
- Singleton is clearer intent: "one system wrapper"

---

### 6. `*Logger` → `factory` Scope

**Pattern:** Class containing `Logger` in name

**Scope:** `factory` - New instance per injection

**Rationale:**
- Stateless logging wrapper
- `isDebugBuild` set once in constructor, never changes
- New instance per consumer prevents unintended coupling
- Memory cost is negligible

**Examples:**
```kotlin
// CORRECT
factory<HiitLogger> {
    val isDebug = (androidContext().applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    HiitLoggerImpl(isDebugBuild = isDebug)
}
// Wrong - creates unnecessary singleton for stateless object
single<HiitLogger> { ... }
```

---

### 7. `*UseCase` → `factory` Scope

**Pattern:** Class ending with `UseCase`

**Scope:** `factory` - New instance per injection

**Rationale:**
- Pure domain business logic (stateless)
- All dependencies are immutable (`private val`)
- Factory ensures clean instances per consumer
- Matches pattern with other stateless objects (Mappers, Interactors, Logger)

**Examples:**
```kotlin
// CORRECT
factory {
    BuildSessionUseCase(
        get(),
        get(named("DefaultDispatcher")),
        get()
    )
}
// Wrong - creates unnecessary singleton for stateless object
single { BuildSessionUseCase(...) }
```

---

### 8. `*ViewModel` → `viewModel` Scope

**Pattern:** Class ending with `ViewModel`

**Scope:** `viewModel` - Special Koin scope tied to AndroidX ViewModel lifecycle

**Rationale:**
- Managed by ViewModelStore
- Survives configuration changes
- Cleared when screen is destroyed
- **Must use `viewModel` scope**, not `single` or `factory`

**Examples:**
```kotlin
// CORRECT
viewModel {
    HomeViewModel(
        get(),
        get(),
        get(named("MainDispatcher")),
        get()
    )
}
```

---

### 9. `*Manager` → `single` Scope

**Pattern:** Class ending with `Manager`

**Scope:** `single` - One instance app-wide

**Rationale:**
- Coordinates complex operations
- Often maintains state
- One manager per domain

**Examples:**
```kotlin
// CORRECT
single<LocaleManager> { LocaleManagerImpl(get(), get(), get()) }

class LocaleManagerImpl(
    private val dataStore: DataStore<Preferences>,
    private val ioDispatcher: CoroutineDispatcher,
    private val logger: HiitLogger
) : LocaleManager {
    // Manages app locale, stores preference
}
```

---

### 10. `*Factory` → `single` Scope

**Pattern:** Class ending with `Factory`

**Scope:** `single` - One instance app-wide

**Rationale:**
- **The factory itself is singleton**
- The factory creates multiple products (which can be short-lived)
- One factory is sufficient to create many instances

**Examples:**
```kotlin
// CORRECT
single<SoundPoolFactory> { SoundPoolFactoryImpl() }

class SoundPoolFactoryImpl : SoundPoolFactory {
    override fun create(): SoundPool = SoundPool.Builder()...
}

// Usage in ViewModel:
class SessionViewModel(
    private val soundPoolFactory: SoundPoolFactory // singleton factory
) : ViewModel() {
    private val soundPool: SoundPool by lazy {
        soundPoolFactory.create() // creates a SoundPool tied to the holding viewmodel scope
    }
}
```

**Key Point:** Don't confuse factory pattern with Koin's `factory` scope
- **Factory pattern:** Object that creates other objects
- **Koin `factory` scope:** Creates new instance per injection

---

### 11. `*Database`, `*Dao` → `single` Scope

**Pattern:** Class containing `Database` or ending with `Dao`

**Scope:** `single` - One instance app-wide

**Rationale:**
- Database: Expensive to create, must be shared
- DAOs: Tied to database instance, one per table

**Examples:**
```kotlin
// CORRECT
single {
    Room.databaseBuilder(
        androidContext(),
        SimpleHiitDatabase::class.java,
        "simplehiit_database"
    ).build()
}

single { get<SimpleHiitDatabase>().usersDao() }
single { get<SimpleHiitDatabase>().sessionsDao() }
```

---

### 12. `*DataStore*` → `single` Scope

**Pattern:** Class containing `DataStore` in name

**Scope:** `single` - One instance app-wide

**Rationale:**
- DataStore: One instance per file
- Multiple instances would cause conflicts

**Examples:**
```kotlin
// CORRECT
single<SimpleHiitDataStoreManager> {
    SimpleHiitDataStoreManagerImpl(
        get(),
        get(named("IoDispatcher")),
        get()
    )
}
```

---

### 13. `*Formatter` → `single` Scope

**Pattern:** Class containing `Formatter` in name

**Scope:** `single` - One instance app-wide

**Rationale:**
- Configured once (format strings from resources)
- Stateless but singleton for convenience
- No need for multiple instances

**Examples:**
```kotlin
// CORRECT
single(named("DigitsFormat")) {
    DurationStringFormatter(
        hoursMinutesSeconds = androidContext().getString(R.string.hours_minutes_seconds_digits),
        ...
    )
}
```

---

### 14. Named `*Dispatcher` → `single` Scope

**Pattern:** Named qualifier containing `Dispatcher`

**Scope:** `single` - One instance app-wide

**Rationale:**
- CoroutineDispatchers are singletons by design
- `Dispatchers.IO`, `Dispatchers.Main`, etc. are global

**Examples:**
```kotlin
// CORRECT
single(named("IoDispatcher")) { Dispatchers.IO }
single(named("MainDispatcher")) { Dispatchers.Main }
single(named("DefaultDispatcher")) { Dispatchers.Default }
single(named("TimerDispatcher")) { Dispatchers.Default }
```
### Automated Safety Verification

**Configuration Tests:**
- `MobileKoinConfigurationTest` - Verifies all mobile app modules load correctly
- `TvKoinConfigurationTest` - Verifies all TV app modules load correctly

These tests catch:
- Missing dependencies
- Circular dependencies
- Type mismatches
- Configuration errors

**Running the tests:**
```bash
# Included automatically when running all unit tests:
./gradlew testDebugUnitTest

# Or run just the configuration tests:
./gradlew :android:mobile:app:testDebugUnitTest --tests "*KoinConfigurationTest"
./gradlew :android:tv:app:testDebugUnitTest --tests "*KoinConfigurationTest"
```

**CI Integration:**
These tests run automatically on every PR via the `testDebugUnitTest` task.
