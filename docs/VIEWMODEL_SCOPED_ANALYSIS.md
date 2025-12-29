# ViewModelComponent-Scoped Dependencies Analysis

## Overview
Analysis of all dependencies that were `@InstallIn(ViewModelComponent::class)` in Hilt to determine if they actually need ViewModel-scoped lifecycle management in Koin.

---

## Dependencies by Module

### shared-ui/home
1. **HomeInteractor** (interface bound to HomeInteractorImpl)
   - **Current:** `factory { HomeInteractorImpl(...) }`
   - **Purpose:** Facade over domain UseCases
   - **State:** STATELESS - just delegates to UseCases
   - **Recommendation:** Could be `factory` or even just inline in ViewModel

2. **HomeViewStateMapper**
   - **Current:** `factory { HomeViewStateMapper(...) }`
   - **Purpose:** Maps domain models to UI state
   - **State:** STATELESS - pure mapping function
   - **Recommendation:** Could be `factory` (lightweight) or `single` (reusable)

---

### shared-ui/session
1. **SessionInteractor** (interface bound to SessionInteractorImpl)
   - **Current:** `factory { SessionInteractorImpl(...) }`
   - **Purpose:** Facade over domain UseCases
   - **State:** STATELESS - just delegates to UseCases
   - **Recommendation:** Could be `factory` or inline in ViewModel

2. **SessionViewStateMapper**
   - **Current:** `factory { SessionViewStateMapper(...) }`
   - **Purpose:** Maps domain models to UI state
   - **State:** STATELESS - pure mapping function
   - **Recommendation:** Could be `factory` (lightweight) or `single` (reusable)

3. **SoundPool** ⚠️
   - **Current:** `factory { SoundPool.Builder()... }`
   - **Purpose:** Android media API for beep sounds
   - **State:** STATEFUL - loads audio files, holds native resources
   - **Lifecycle Requirements:**
     - Created in ViewModel init
     - Sound file loaded asynchronously
     - Must be released in ViewModel.onCleared()
     - Survives configuration changes (with ViewModel)
   - **Recommendation:** MUST stay `factory` - one per ViewModel instance
   - **Critical:** Already properly managed - released in SessionViewModel.onCleared()

---

### shared-ui/settings
1. **SettingsInteractor** (interface bound to SettingsInteractorImpl)
   - **Current:** `factory { SettingsInteractorImpl(...) }`
   - **Purpose:** Facade over domain UseCases (21 of them!)
   - **State:** STATELESS - just delegates to UseCases
   - **Recommendation:** Could be `factory` or inline in ViewModel

2. **SettingsViewStateMapper**
   - **Current:** `factory { SettingsViewStateMapper(...) }`
   - **Purpose:** Maps domain models to UI state
   - **State:** STATELESS - pure mapping function
   - **Recommendation:** Could be `factory` (lightweight) or `single` (reusable)

---

### shared-ui/statistics
1. **StatisticsInteractor** (interface bound to StatisticsInteractorImpl)
   - **Current:** `factory { StatisticsInteractorImpl(...) }`
   - **Purpose:** Facade over domain UseCases
   - **State:** STATELESS - just delegates to UseCases
   - **Recommendation:** Could be `factory` or inline in ViewModel

2. **StatisticsViewStateMapper**
   - **Current:** `factory { StatisticsViewStateMapper(...) }`
   - **Purpose:** Maps domain models to UI state
   - **State:** STATEFUL? - Injects FormatLongDurationMsAsSmallestHhMmSsStringUseCase
   - **Recommendation:** Could be `factory` (lightweight) or `single` (reusable)

---

## Summary by Component Type

### Interactors (4 total)
- **Current Hilt:** `@InstallIn(ViewModelComponent::class)` with `@Binds`
- **Current Koin:** `factory<Interface> { Implementation(...) }`
- **State:** All STATELESS
- **Why ViewModelComponent in Hilt?**
  - Hilt best practice for ViewModel dependencies
  - Ensures one instance per ViewModel
  - Automatically cleaned up with ViewModel
- **Actually needs ViewModel scope?** NO
  - No state to preserve
  - No resources to clean up
  - Just a facade pattern

**Options for Koin:**
1. ✅ **Keep `factory`** - One per ViewModel (current approach)
   - Pros: Matches Hilt behavior, clear lifecycle
   - Cons: Slight overhead creating new instances

2. **Change to `single`** - One instance app-wide
   - Pros: No creation overhead
   - Cons: Shared across ViewModels (unlikely to cause issues since stateless)

3. **Inline in ViewModel** - Remove abstraction
   - Pros: Simplest, no DI needed
   - Cons: Loses abstraction, harder to test

**Recommendation:** Keep `factory` for consistency and testability

---

### ViewStateMappers (4 total)
- **Current Hilt:** `@InstallIn(ViewModelComponent::class)` with `@Inject constructor`
- **Current Koin:** `factory { ViewStateMapper(...) }`
- **State:** All STATELESS (pure functions)
- **Why ViewModelComponent in Hilt?**
  - Injected into ViewModel
  - Hilt convention for ViewModel dependencies
- **Actually needs ViewModel scope?** NO
  - Pure mapping logic
  - No state between calls
  - Could be shared safely

**Options for Koin:**
1. ✅ **Keep `factory`** - One per ViewModel (current approach)
   - Pros: Lightweight objects, clear ownership
   - Cons: Recreated for each ViewModel

2. **Change to `single`** - One instance app-wide
   - Pros: Reusable across all ViewModels
   - Cons: Slightly less clear ownership (minimal issue)

**Recommendation:** Change to `single` - they're stateless and reusable

---

### SoundPool (1 total) ⚠️ CRITICAL
- **Current Hilt:** `@ViewModelScoped` (not just ViewModelComponent!)
- **Current Koin:** `factory { SoundPool.Builder()... }`
- **State:** STATEFUL - holds native audio resources
- **Why @ViewModelScoped in Hilt?**
  - Must survive configuration changes
  - Must be released when ViewModel is destroyed
  - Loads audio file asynchronously
- **Actually needs ViewModel scope?** YES - ABSOLUTELY

**Must stay `factory`:**
- Created when SessionViewModel is instantiated
- Sound file loaded in ViewModel.init
- Released in SessionViewModel.onCleared()
- New instance per ViewModel instance
- Cannot be shared between ViewModels

**Recommendation:** Keep `factory` - CORRECTLY IMPLEMENTED ✅

---

## Recommendations Summary

### High Priority Changes
1. **ViewStateMappers** → Change to `single`
   - All 4 mappers (Home, Session, Settings, Statistics)
   - Stateless, reusable, lightweight
   - Reduces object creation overhead

### Low Priority Changes
2. **Interactors** → Could change to `single`
   - All 4 interactors
   - Stateless facades
   - Currently `factory` works fine
   - Changing has minimal benefit

### Must Not Change
3. **SoundPool** → MUST stay `factory` ⚠️
   - Stateful resource
   - Proper lifecycle management critical
   - Already correctly implemented

---

## Proposed Changes

### Option A: Conservative (Recommended)
Only change ViewStateMappers to `single`:
```kotlin
// In each shared-ui module
val xxxModule = module {
    single { XxxViewStateMapper(...) }  // Changed from factory
    factory<XxxInteractor> { XxxInteractorImpl(...) }  // Keep as-is
    viewModel { XxxViewModel(...) }
}
```

### Option B: Aggressive
Change both Mappers and Interactors to `single`:
```kotlin
val xxxModule = module {
    single { XxxViewStateMapper(...) }
    single<XxxInteractor> { XxxInteractorImpl(...) }
    viewModel { XxxViewModel(...) }
}
```

### Option C: Keep Current
No changes - current implementation is valid and works correctly.

---

## Testing Considerations

If we change to `single`:
- **ViewStateMappers**: Should pass all tests (pure functions)
- **Interactors**: Should pass all tests (stateless facades)
- **Important**: Verify no hidden state in any mapper/interactor

---

## Koin Scoping Clarification

### `single` in Koin
- **Meaning:** Singleton - ONE instance for the ENTIRE app lifecycle
- **Created:** Once, on first request
- **Reused:** Every injection point gets the same instance
- **Destroyed:** When Koin context is stopped (app shutdown)
- **Example:** Database, shared preferences, app-level settings

### `factory` in Koin
- **Meaning:** New instance every time
- **Created:** Every time `get()` is called
- **Reused:** Never - always creates new instance
- **Destroyed:** When owning object is garbage collected
- **Example:** ViewModels, temporary objects

### `viewModel` in Koin
- **Meaning:** ViewModel-scoped instance
- **Created:** When ViewModel is first requested
- **Reused:** Same instance during ViewModel lifecycle (survives config changes)
- **Destroyed:** When ViewModel is cleared
- **Example:** All our ViewModels

---

## Revised Recommendation

**Actually, keep EVERYTHING as `factory` (Option C)**

### Why?

1. **ViewStateMappers** - Keep as `factory`
   - They're lightweight objects (just hold dependencies)
   - Creating one per ViewModel is negligible overhead
   - `factory` makes ownership clearer (belongs to that ViewModel)
   - `single` would mean ONE mapper shared across ALL ViewModels forever
   - No benefit to singleton - they're stateless but cheap to create

2. **Interactors** - Keep as `factory`
   - Same reasoning as mappers
   - Lightweight facades
   - Clear ownership per ViewModel
   - No benefit to singleton

3. **SoundPool** - MUST stay `factory`
   - Holds native resources
   - Must be released in ViewModel.onCleared()
   - Cannot be shared

### Singleton Anti-Pattern
Making these `single` would mean:
- One HomeViewStateMapper instance for entire app
- Shared between all HomeViewModel instances
- Lives forever (until app shutdown)
- No actual benefit since they're stateless
- Less clear ownership

### Current Implementation is Correct ✅
The `factory` approach for all non-ViewModel dependencies is the right choice:
- Clear lifecycle (created with ViewModel, GC'd with ViewModel)
- No shared state risks
- Minimal overhead (these objects are lightweight)
- Matches the Hilt ViewModelComponent semantics

---

---

## SoundPool: Factory vs ViewModel Scope

### The Question
"Why not use Koin's ViewModel scoping for SoundPool since it needs ViewModel lifecycle?"

### Options for SoundPool

#### Option 1: `factory` (Current Implementation)
```kotlin
val sessionModule = module {
    factory {
        SoundPool.Builder()...build()
    }

    viewModel {
        SessionViewModel(
            soundPool = get(),  // Creates new SoundPool
            ...
        )
    }
}

// In SessionViewModel
override fun onCleared() {
    soundPool.release()  // Manual cleanup
}
```

**Pros:**
- Simple and straightforward
- Clear ownership (ViewModel owns the instance)
- Explicit cleanup in onCleared()

**Cons:**
- Lifecycle relationship not explicit in DI config
- Must remember to clean up in onCleared()

---

#### Option 2: ViewModel Scope (More Explicit)
```kotlin
val sessionModule = module {
    scope<SessionViewModel> {
        scoped {
            SoundPool.Builder()...build()
        }
    }

    viewModel {
        SessionViewModel(
            sessionScope = getScope(),  // Pass the scope
            ...
        )
    }
}

// In SessionViewModel
class SessionViewModel(
    private val sessionScope: Scope,
    ...
) : ViewModel() {

    private val soundPool: SoundPool by sessionScope.inject()

    override fun onCleared() {
        soundPool.release()
        sessionScope.close()  // Clean up scope
    }
}
```

**Pros:**
- Lifecycle relationship explicit in DI configuration
- SoundPool is formally scoped to ViewModel
- scope.close() can clean up multiple scoped dependencies

**Cons:**
- More complex (need to inject and manage Scope)
- Still need manual cleanup in onCleared()
- Changes ViewModel constructor signature

---

---

#### Option 3: Factory Pattern (Most Testable) ⭐
```kotlin
// Define factory interface
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
    viewModel { SessionViewModel(soundPoolFactory = get(), ...) }
}

// In SessionViewModel
class SessionViewModel(
    private val soundPoolFactory: SoundPoolFactory,
    ...
) : ViewModel() {

    private val soundPool: SoundPool by lazy { soundPoolFactory.create() }

    override fun onCleared() {
        if (::soundPool.isInitialized) {
            soundPool.release()
        }
    }
}
```

**Pros:**
- **Best testability**: Easy to mock SoundPoolFactory
- **Single as singleton**: Factory can be `single` (stateless)
- **Lazy creation**: SoundPool created only when needed
- **Clear lifecycle**: Created in ViewModel, released in onCleared()
- **No Koin-specific types**: ViewModel doesn't need Scope

**Cons:**
- Requires creating factory interface/implementation
- Slightly more code

---

### Comparison of All Options

| Aspect | Factory (Option 1) | ViewModel Scope (Option 2) | Factory Pattern (Option 3) |
|--------|-------------------|---------------------------|---------------------------|
| **Simplicity** | ✅ Simple | ❌ Complex | ✅ Simple |
| **Testability** | ⚠️ Must mock SoundPool | ⚠️ Must mock Scope | ✅ Easy to mock factory |
| **Lifecycle Clarity** | ⚠️ Implicit | ✅ Explicit | ✅ Clear |
| **SoundPool Scope** | Per ViewModel | Per ViewModel | Per ViewModel |
| **Factory Scope** | N/A | N/A | ✅ Singleton |
| **ViewModel Knows About** | SoundPool (Android API) | Koin Scope | Factory (own interface) |
| **Lines of Code** | Low | Medium | Medium |

---

### Why Current Implementation Uses `factory` (Option 1)

1. **Simplicity**: The `factory` approach is simpler - ViewModel just gets SoundPool in constructor
2. **Explicit Cleanup**: We already need onCleared() to call soundPool.release()
3. **Single Dependency**: Only one dependency (SoundPool) needs this lifecycle, so scope overhead not justified
4. **Constructor Clarity**: ViewModel constructor takes concrete dependencies, not a Scope

### Why Factory Pattern (Option 3) Is Better

1. **Testability**: Mocking a factory interface is much easier than mocking SoundPool
2. **Separation of Concerns**: ViewModel doesn't know about SoundPool creation details
3. **Factory Reusability**: Factory can be singleton - stateless and reusable
4. **No Framework Dependencies**: ViewModel doesn't depend on Koin types (Scope)

### When to Use ViewModel Scope

Use Koin's ViewModel scope when:
- **Multiple** dependencies need ViewModel lifecycle
- You want DI config to explicitly show lifecycle relationships
- You have complex cleanup logic that benefits from scope.close()

### Recommendation for This Project

**Keep `factory` for SoundPool** because:
- It's the only dependency that needs ViewModel lifecycle in SessionViewModel
- Cleanup is simple: just soundPool.release()
- Simpler code, less indirection
- Works perfectly fine

---

**Final Recommendation:** **Keep current implementation unchanged**

The migration is already correct. The `factory` approach for SoundPool is appropriate given it's a single dependency with simple cleanup.
