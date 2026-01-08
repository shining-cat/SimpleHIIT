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

* **User object: expose timestamp of last session for sorting on home** - Improves UX, straightforward domain model enhancement.
* maybe replace InputError.NONE by a nullable?
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

---

## Architecture Assessment & Feedback

### Current State Analysis (2026-01-08)

#### ✅ Excellent Adherence to Android Best Practices

1. **Separation of Concerns**
   - Clean layering: UI → ViewModel → Presenter → Interactor → UseCase → Repository
   - ViewModels are thin wrappers focusing solely on lifecycle management
   - Presenters contain pure business logic with zero Android dependencies
   - Domain layer is completely framework-agnostic

2. **Dependency Management**
   - Convention plugins properly centralize external dependencies
   - Module dependencies follow strict unidirectional flow
   - No circular dependencies detected
   - Clear separation between Android modules and pure Kotlin modules

3. **Testing Architecture**
   - Presenters fully unit testable (pure Kotlin, easily mockable)
   - Domain layer testable without Android framework
   - Test utilities properly modularized and reusable
   - Good use of test doubles (mockk) and coroutine testing patterns

4. **Dependency Injection**
   - Koin properly configured with module-specific DI files
   - Clear separation between factory and singleton scopes
   - Platform-specific and shared components properly scoped

5. **Build Configuration**
   - Gradle convention plugins promote consistency
   - Version catalog centralized in `libs.versions.toml`
   - Proper use of Kotlin DSL
   - Build-logic module ensures build configuration reusability

#### ✅ Strong KMP Readiness Indicators

1. **Pure Kotlin Shared Logic**
   - shared-ui presenters have no Android imports
   - Domain layer completely platform-agnostic
   - Models are pure Kotlin data classes
   - Flow-based reactive architecture (KMP-compatible)

2. **Proper Abstraction Layers**
   - HiitLogger abstraction ready for expect/actual
   - No direct Android framework usage in shared-ui or domain
   - Repository pattern enables platform-specific implementations

3. **Modular Architecture**
   - Feature-based modules easily map to KMP source sets
   - Clear boundaries between platform (android/*) and shared (shared-ui/*, domain/*)
   - Common utilities properly isolated

#### ⚠️ Areas for Improvement

1. **Testing Gaps** (Already identified in Priority 1)
   - No integration tests validating cross-module interactions
   - No UI tests for Compose screens
   - No user-flow tests covering end-to-end scenarios
   - ViewModel layer lacks test coverage
   - Screenshot tests not yet implemented

2. **KMP Migration Blockers**
   - shared-ui modules still use `simplehiit.android.library` plugin
   - AndroidManifest.xml still present in shared-ui modules (unnecessary for pure Kotlin)
   - No KMP source sets configured (commonMain, androidMain, etc.)
   - No platform-specific implementations using expect/actual pattern

3. **Architecture Debt (Minor)**
   - Some shared-ui modules still named under Android package structure (`java/` directory instead of `kotlin/`)
   - Could benefit from explicit KDoc on public APIs
   - Some test naming could be more consistent

4. **Performance & Monitoring**
   - No performance benchmarks for critical paths (session timer accuracy)
   - No crash reporting or analytics infrastructure visible
   - No logging strategy for production (HiitLogger configuration unclear)

5. **Build & CI**
   - Test coverage reporting configured (Kover) but thresholds not enforced
   - No visible CI/CD workflows (GitHub Actions, etc.)
   - No automated dependency updates (Dependabot, Renovate)

#### 🎯 Critical Path to KMP Success

1. **Immediate Actions** (Before KMP conversion)
   - Add integration tests to establish baseline behavior
   - Add ViewModel tests to ensure lifecycle logic is sound
   - Document expect/actual strategy for HiitLogger

2. **KMP Conversion** (Can start once testing is solid)
   - Convert one shared-ui module as pilot (suggest home module - simplest)
   - Change plugin from `simplehiit.android.library` to `org.jetbrains.kotlin.multiplatform`
   - Configure source sets (commonMain, commonTest)
   - Remove AndroidManifest.xml
   - Verify all tests still pass
   - Repeat for remaining shared-ui modules

3. **Post-Conversion Validation**
   - Run tests on multiple platforms
   - Add iOS-specific tests (when iOS target added)
   - Verify no Android leakage in shared code

#### 📊 Overall Assessment

**Grade: A- (Very Strong Architecture)**

**Strengths:**
- Exemplary separation of concerns
- Clean dependency graph
- Production-ready testing for presenters
- Well-positioned for KMP migration
- Modern Android best practices (Compose, Flow, Kotlin Coroutines)
- Proper modularization strategy

**Weaknesses:**
- Testing coverage gaps at integration/UI layers
- One step away from true KMP (plugin conversion needed)
- Missing production monitoring/observability

**Recommendation:**
The architecture is **production-ready from a design perspective** but needs **testing infrastructure expansion** before confident deployment. The KMP migration is **remarkably close** - the hard work of extracting pure Kotlin logic is complete. Focus on Priority 1 (testing) and Priority 2 (KMP conversion) in that order.

**This project serves as an excellent learning platform for:**
- Modern Android architecture patterns (MVVM + Clean Architecture)
- Kotlin multiplatform preparation
- Comprehensive testing strategies
- Gradle build configuration best practices
- Dependency injection with Koin
- Modular project structure

The deliberate focus on learning and best practices over minimalism has resulted in a well-architected, maintainable codebase that closely follows industry standards.
