# Threading Strategy

This document explains the threading choices made in this project.

## Overview

The current recommendation is to [inject dispatchers](https://developer.android.com/kotlin/coroutines/coroutines-best-practices#inject-dispatchers), so tests can then inject their own and manipulate them as needed.

All architecture layers follow the principle that [all suspend methods should be safe to call from the main thread](https://developer.android.com/kotlin/coroutines/coroutines-best-practices#main-safe).

## Thread Assignment by Layer

### Presentation Layer - Main Thread

**Dispatcher**: `Main`

The main dispatcher is injected into ViewModels, as they have UI-related responsibilities.

ViewModels rely on the `viewModelScope`, which has the advantage of being lifecycle-aware. They specify the injected dispatcher as the one that scope should use.

This strategy is made possible by following the recommendation that all suspend methods should be safe to call from the main thread.

### Data Layer - IO Thread

**Dispatcher**: `IO`

The Data layer (through its only entry point, the repository implementation) is responsible for all usages of the IO thread. This dispatcher is injected into `SimpleHiitRepositoryImpl`.

This choice is dictated by the fact that the data layer is the one aware of any IO operations, if any.

Thus, all suspend methods in the `SimpleHiitRepository` include a thread switch towards the IO dispatcher.

### Domain Layer - Default Thread

**Dispatcher**: `Default`

The Domain layer, as the central point of the clean architecture, exposes suspend methods to the Presentation layer. Some methods include computation work, while others simply call and handle Data layer suspend methods.

Since the Data layer is responsible for picking the adequate thread to use in its own suspend methods, the Domain layer has nothing to do with the IO thread.

Only the Default dispatcher is injected in this layer, to be used by suspend methods in the use cases.

## Thread Flow Summary

```
┌──────────────────┐
│  Presentation    │  Main thread
│  (ViewModels)    │  (lifecycle-aware viewModelScope)
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│     Domain       │  Default thread
│   (Use Cases)    │  (computation work)
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│      Data        │  IO thread
│  (Repository)    │  (database, network, file operations)
└──────────────────┘
```

## Testing Benefits

By injecting dispatchers:
- Tests can inject `TestDispatcher` for all layers
- Control over coroutine execution in tests
- Deterministic test behavior
- Easy to test threading-related logic

## References

- [Kotlin Coroutines Best Practices - Inject Dispatchers](https://developer.android.com/kotlin/coroutines/coroutines-best-practices#inject-dispatchers)
- [Kotlin Coroutines Best Practices - Main-Safe](https://developer.android.com/kotlin/coroutines/coroutines-best-practices#main-safe)
