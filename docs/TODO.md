# SimpleHIIT ToDo list

## Priority 1: Testing Enhancements
**High Impact | Medium Effort | Critical for Production Readiness**

* **Add integration tests for shared-ui layer** - Test presenter + interactor + mapper together without mocking everything. Validates end-to-end flows within shared-ui modules. Should cover critical user journeys (start session, modify settings, view stats).
* **Add user-flow/journey tests** - End-to-end tests simulating real user scenarios across multiple screens. Examples: "Create user → Configure settings → Run session → View statistics". Tests navigation, state persistence, and cross-feature integration.
* **Add UI tests (Compose Testing)** - Test Android UI layer with Compose testing framework. Validate composables render correctly, user interactions work, and UI state updates properly. Focus on critical screens first (Home, Session).
* **Add screenshot tests** - Regression testing for visual consistency across devices and configurations. Verify layouts, theming, and responsive design. Free on GitHub Actions (investigate Paparazzi or Roborazzi).
* **Add ViewModel tests** - ViewModels currently lack test coverage. Test lifecycle-aware behavior, state collection, coroutine scoping, and delegation to presenters.

## Priority 2: KMP Migration Preparation
**High Impact | Medium-High Effort | Blockers for True Multiplatform**

* **Migrate shared-ui modules from Android Library to KMP** - Convert `simplehiit.android.library` to `org.jetbrains.kotlin.multiplatform` plugin. Remove Android-specific artifacts (AndroidManifest.xml). This is the final step to make shared-ui truly multiplatform-ready.
* **Add expect/actual implementations for platform-specific dependencies** - Currently uses HiitLogger (could be platform-agnostic). Identify and abstract any remaining platform-specific code.
* **Configure KMP source sets** - Set up commonMain, androidMain, iosMain, etc. Ensure domain and shared-ui layers compile for all target platforms.

## Priority 3: Quick Wins
**Low-Medium Impact | Low Effort | Can be done now, won't interfere with migration**

* **Find publication strategy**: Fdroid, github, or self-hosted - Research task, determines distribution approach early.

## Priority 4: Pre-Migration Improvements
**Medium Impact | Medium Effort | Better done before KMP migration**

* **Refine French and Swedish translations** - Easier to manage in current structure; localization strategy may change with KMP.
* **Allow session to run as timer-only when no exercise types selected** - Feature enhancement that touches session flow; stabilize before migration.
* **Improve UI arrangement bucketing for large displays in portrait** - Android-specific responsive layout fix; address while architecture is familiar.

## Priority 5: Android-Specific Optimizations
**Medium Impact | Low-Medium Effort | Android-only, deprioritize until after KMP**

* **Use [compose stability plugin](https://proandroiddev.com/compose-stability-analyzer-real-time-stability-insights-for-jetpack-compose-1399924a0a64)** - Android Compose optimization; wait until shared-ui architecture stabilizes post-migration.

## Priority 6: Platform Expansion
**Variable Impact | High Effort | Pursue after KMP foundation is solid**

* **Complete KMP migration** - Final conversion of shared-ui modules to remove remaining Android library plugin, enabling true multiplatform compilation.
* **Explore Wear OS support** - Check [Google sample for Watch](https://github.com/android/wear-os-samples/tree/main/WearVerifyRemoteApp). Android-specific platform, but good KMP testing ground.
* **Build for other platforms** (iOS, Desktop, Web) - Main KMP goal, pursue after shared-ui modules are fully multiplatform-ready.

---

## Migration Context Notes
- ✅ **Completed**: Hilt → Koin migration
- ✅ **Completed**: shared-ui module extraction with pure Kotlin presenters
- ✅ **Completed**: Comprehensive presenter unit tests (91 tests, ~93% coverage)
- 🚧 **In Progress**: Testing infrastructure expansion (integration, UI, screenshot tests)
- 🎯 **Next Focus**: Migrate shared-ui from Android Library to KMP plugin
