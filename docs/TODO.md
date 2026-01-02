# SimpleHIIT ToDo list

## Priority 1: KMP Migration Preparation
**High Impact | Medium-High Effort | Blockers for KMP Success**

* **Extract platform-agnostic logic from viewmodels** - Critical for shared-ui to become truly multiplatform. ViewModels currently contain Android-specific dependencies that need to be abstracted into use cases/domain layer.
* **Add tests on viewmodels after making them lighter** - Essential to ensure migration doesn't break functionality. Testing infrastructure will validate the extracted logic works correctly across platforms.

## Priority 2: Quick Wins
**Low-Medium Impact | Low Effort | Can be done now, won't interfere with migration**

* **Statistics: remove seconds from displays to reduce clutter** (when value > 1h) - Minor UI polish, minimal code change.
* **User object: expose timestamp of last session for sorting on home** - Improves UX, straightforward domain model enhancement.
* **Find publication strategy**: Fdroid, github, or self-hosted - Research task, determines distribution approach early.

## Priority 3: Pre-Migration Improvements
**Medium Impact | Medium Effort | Better done before KMP migration**

* **Refine French and Swedish translations** - Easier to manage in current structure; localization strategy may change with KMP.
* **Allow session to run as timer-only when no exercise types selected** - Feature enhancement that touches session flow; stabilize before migration.
* **Improve UI arrangement bucketing for large displays in portrait** - Android-specific responsive layout fix; address while architecture is familiar.
* **Screenshot tests (if free on GitHub)** - Testing infrastructure setup; establish patterns before multiplatform complexity.

## Priority 4: Android-Specific Optimizations
**Medium Impact | Low-Medium Effort | Android-only, deprioritize until after KMP**

* **Use [compose stability plugin](https://proandroiddev.com/compose-stability-analyzer-real-time-stability-insights-for-jetpack-compose-1399924a0a64)** - Android Compose optimization; wait until shared-ui architecture stabilizes post-migration.

## Priority 5: Platform Expansion
**Variable Impact | High Effort | Pursue after KMP foundation is solid**

* **Complete KMP migration** - Convert shared-ui modules to remove Android framework dependencies, enabling true multiplatform code sharing.
* **Explore Wear OS support** - Check [Google sample for Watch](https://github.com/android/wear-os-samples/tree/main/WearVerifyRemoteApp). Android-specific platform, but good KMP testing ground.
* **Build for other platforms** (iOS, Desktop, Web) - Main KMP goal, pursue after Android modules are fully multiplatform-ready.

---

## Migration Context Notes
- ✅ **Completed**: Hilt → Koin migration
- 🚧 **In Progress**: shared-ui module created (still Android-tied)
- 🎯 **Next Focus**: Decouple shared-ui from Android framework dependencies
