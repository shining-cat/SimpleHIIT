# Comprehensive TV vs Mobile Comparison

## Executive Summary

This document provides an exhaustive comparison of the TV and Mobile implementations in the SimpleHIIT project, identifying feature discrepancies and implementation inconsistencies to inform the creation of a platform-agnostic shared-ui module.

---

## 1. Build Configuration & Dependencies

### Similarities
- Both use the same plugin structure (simplehiit.android.library, simplehiit.hilt, simplehiit.testing)
- Both depend on the same core modules: `android.common`, `domain.common`, `commonUtils`, `commonResources`
- Module structure is parallel (app, ui/common, ui/home, ui/session, ui/settings, ui/statistics)

### Differences

| Aspect | Mobile | TV |
|--------|--------|-----|
| **Compose Plugin** | `simplehiit.mobile.compose.ui` | `simplehiit.tv.compose.ui` |
| **App Plugin** | `simplehiit.android.application.handheld` | `simplehiit.android.application.tv` |
| **Compose Material** | `androidx.compose.material3` | `androidx.tv.material3` |
| **Adaptive Layout** | `androidx.compose.adaptive` ✅ | ❌ Not included |
| **TV Foundation** | ❌ | `androidx.tv.foundation` ✅ |

**Impact:** Different Compose Material libraries create significant UI component divergence.

---

## 2. UI Common Module Comparison

### 2.1 File Structure Differences

#### Mobile-Only Files
- **`UiArrangement.kt`** - Enum for VERTICAL/HORIZONTAL layouts
- **`components/NavigateUpTopBar.kt`** - Top navigation bar with back button
- **`components/OnSurfaceTextButton.kt`** - Specific button component
- **`components/ToggleButton.kt`** - Toggle button component
- **`components/ErrorDialog.kt`** - Error dialog
- **`components/WarningDialog.kt`** - Warning dialog
- **`helpers/AdaptiveDialogButtonsLayout.kt`** - Adaptive button layout system
- **`helpers/LayoutHelpers.kt`** - Layout utility functions
- **`helpers/WindowSizeClassHelpers.kt`** - Window size class utilities
- **`theme/MobileDarkColorScheme.kt`** - Mobile-specific dark theme
- **`theme/MobileLightColorScheme.kt`** - Mobile-specific light theme
- **`theme/MobileType.kt`** - Mobile typography

#### TV-Only Files
- **`components/ButtonBordered.kt`** - Bordered button
- **`components/ButtonContentLayout.kt`** - Button content layout helper
- **`components/ButtonError.kt`** - Error button
- **`components/ButtonFilled.kt`** - Filled button
- **`components/ButtonIcon.kt`** - Icon button
- **`components/ButtonText.kt`** - Text button
- **`components/ButtonToggle.kt`** - Toggle button (different from mobile)
- **`components/CustomCircularProgressIndicator.kt`** - Custom circular progress
- **`components/CustomLinearProgressIndicator.kt`** - Custom linear progress
- **`components/DialogContentLayout.kt`** - Dialog content layout wrapper
- **`components/DialogError.kt`** - Error dialog
- **`components/DialogInput.kt`** - Input dialog
- **`components/DialogWarning.kt`** - Warning dialog
- **`components/RadioButtonsDialog.kt`** - Radio button selection dialog
- **`theme/TvDarkColorScheme.kt`** - TV-specific dark theme
- **`theme/TvLightColorScheme.kt`** - TV-specific light theme
- **`theme/TvShapes.kt`** - TV shape definitions
- **`theme/TvType.kt`** - TV typography

### 2.2 Shared Files with Different Implementations

#### AboutScreen.kt
**Mobile:**
- Supports both VERTICAL and HORIZONTAL `UiArrangement`
- Has `NavigateUpTopBar` in vertical mode
- Has `NavigationSideBar` in horizontal mode
- Separate composables: `AboutContentVertical()` and `AboutContentHorizontal()`
- Uses Material3 components

**TV:**
- Single layout only (always horizontal with sidebar)
- No UiArrangement parameter
- Always includes `NavigationSideBar`
- Single composable: `AboutContent()`
- Uses TV Material3 components with `@ExperimentalTvMaterial3Api`

**Similarities:**
- Same content structure and text
- Same image resources
- Same clickable links logic

#### BasicLoading.kt
**Mobile:**
- Simple implementation using Material3 `CircularProgressIndicator`
- Includes `WindowInsets.safeDrawing` padding
- ~16 lines of code

**TV:**
- Complex custom animation with infinite transitions
- Uses `CustomCircularProgressIndicator` with rotation and alpha animations
- Configurable `halfCycleDurationMs` and `color` parameters
- ~95 lines of code with animation logic

**Rationale:** TV needs more sophisticated loading animations for 10-foot UI

#### NavigationSideBar.kt & SideBarItem.kt
**Mobile:**
- Uses Material3 `Icon`, `IconButton`, `Text`
- Simpler interaction model (click)
- Includes `WindowInsets.safeDrawing` padding

**TV:**
- Uses TV Material3 `Surface` with focus management
- D-pad navigation support
- More complex focus visual states
- Uses `ClickableSurfaceDefaults`

---

## 3. Home Module Comparison

### 3.1 Identical Components (Can be Shared)
- ✅ **`HomeViewModel.kt`** - Identical logic (TV has 2 extra comment lines)
- ✅ **`HomeViewState.kt`** - Identical sealed class
- ✅ **`HomeViewStateMapper.kt`** - Identical mapping logic
- ✅ **`HomeInteractor.kt`** - Identical business logic
- ✅ **`HomeModule.kt`** (Hilt DI) - Identical
- ✅ **`contents/HomeContentHolder.kt`** - Very similar
- ✅ **`contents/HomeErrorContent.kt`** - Very similar
- ✅ **`contents/HomeMissingUsersContent.kt`** - Very similar
- ✅ **`contents/HomeNominalContent.kt`** - Very similar
- ✅ **`components/NumberCyclesComponent.kt`** - Very similar
- ✅ **`components/SingleUserHeaderComponent.kt`** - Very similar

### 3.2 Different Implementations

#### HomeScreen.kt
**Mobile:**
- Uses `Row` with conditional `AnimatedVisibility` for `NavigationSideBar`
- Has `HomeTopBarComponent` in vertical mode
- `UiArrangement` parameter for responsive layout
- Material3 components

**TV:**
- Uses `NavigationDrawer` with sidebar in drawer
- No top bar
- No UiArrangement concept
- TV Material3 with `@ExperimentalTvMaterial3Api`

#### SelectUsersComponent
**Mobile:**
- Two separate files:
  - `SelectUsersComponentHorizontal.kt`
  - `SelectUsersComponentVertical.kt`
- Adaptive to UiArrangement

**TV:**
- Single `SelectUsersComponent.kt`
- Always horizontal layout

#### HomeTopBarComponent
**Mobile Only:**
- Top app bar with "Go to Settings" button
- Not applicable for TV (always has sidebar)

### 3.3 Components Flow

Both platforms follow the same pattern:
1. `HomeScreen` (entry point, collects ViewModels)
2. `HomeContentHolder` (switches between states)
3. State-specific content composables
4. Reusable components

---

## 4. Session Module Comparison

### 4.1 Identical Components (Can be Shared)
- ✅ **`SessionViewModel.kt`** - Identical
- ✅ **`SessionViewState.kt`** - Identical
- ✅ **`SessionViewStateMapper.kt`** - Identical
- ✅ **`SessionInteractor.kt`** - Identical
- ✅ **`SessionModule.kt`** - Identical
- ✅ **`components/CountDownComponent.kt`** - Very similar
- ✅ **`components/ExerciseDescriptionComponent.kt`** - Very similar
- ✅ **`components/ExerciseDisplayComponent.kt`** - Very similar
- ✅ **`components/PauseDialog.kt`** - Very similar
- ✅ **`components/RemainingPercentageComponent.kt`** - Very similar
- ✅ **`components/RunningSessionStepInfoDisplayComponent.kt`** - Very similar
- ✅ **`contents/SessionContentHolder.kt`** - Very similar
- ✅ **`contents/SessionErrorStateContent.kt`** - Very similar
- ✅ **`contents/SessionFinishedContent.kt`** - Very similar
- ✅ **`contents/SessionPrepareContent.kt`** - Very similar
- ✅ **`contents/SessionRunningNominalContent.kt`** - Very similar

### 4.2 Different Implementations

#### SessionScreen.kt
**Mobile:**
- Uses `UiArrangement` for responsive layout
- Conditional `NavigationSideBar` based on arrangement
- File: `SessionScreenPreview.kt`

**TV:**
- Uses `NavigationDrawer`
- Always includes sidebar
- File: `SessionScreenTvPreview.kt`

#### SessionNavigationSideBar / SessionSideBarComponent
**Mobile:**
- `SessionSideBarComponent.kt` - Simpler mobile interaction

**TV:**
- `SessionNavigationSideBar.kt` - D-pad focused navigation

---

## 5. Settings Module Comparison

### 5.1 Identical Components (Can be Shared)
- ✅ **`SettingsViewModel.kt`** - Identical
- ✅ **`SettingsViewState.kt`** - Identical
- ✅ **`SettingsViewStateMapper.kt`** - Identical
- ✅ **`SettingsInteractor.kt`** - Identical
- ✅ **`SettingsModule.kt`** - Identical
- ✅ **`components/SettingsExercisesSelectedComponent.kt`** - Very similar
- ✅ **`components/SettingsFieldComponent.kt`** - Very similar
- ✅ **`components/SettingsToggleComponent.kt`** - Very similar
- ✅ **`components/SettingsUsersComponent.kt`** - Very similar
- ✅ **`contents/SettingsContentHolder.kt`** - Very similar
- ✅ **`contents/SettingsErrorContent.kt`** - Very similar
- ✅ **`contents/SettingsNominalContent.kt`** - Very similar

### 5.2 Dialog Implementations - Major Difference

#### Mobile Dialogs
- **`dialogs/InputDialog.kt`** - Sophisticated adaptive input dialog
  - Uses Material3 `Dialog` and `OutlinedTextField`
  - Has `AdaptiveDialogButtonsLayout` for responsive button arrangement
  - Complex layout calculations for field sizing
  - ~430 lines

- **Individual dialog files:**
  - `SettingsAddUserDialog.kt`
  - `SettingsEditNumberCyclesDialog.kt`
  - `SettingsEditPeriodLengthDialog.kt`
  - `SettingsEditPeriodStartCountDownDialog.kt`
  - `SettingsEditSessionStartCountDownDialog.kt`
  - `SettingsEditUserDialog.kt`
  - `SettingsPickLanguageDialog.kt`
  - `SettingsPickThemeDialog.kt`

#### TV Dialogs
- Uses `DialogInput` from `tv/ui/common/components/`
- Uses `RadioButtonsDialog` from `tv/ui/common/components/`
- Uses `BasicTextField` instead of `OutlinedTextField`
- Custom decoration box for input fields
- Fixed button layout (always horizontal Row)
- ~450 lines

**Key Difference:** Mobile has adaptive button layouts based on screen size, TV has fixed layouts optimized for 10-foot UI.

#### SettingsScreen.kt
**Mobile:**
- `UiArrangement` responsive layout
- Conditional sidebar/top bar

**TV:**
- `NavigationDrawer` layout
- Always sidebar

---

## 6. Statistics Module Comparison

### 6.1 Identical Components (Can be Shared)
- ✅ **`StatisticsViewModel.kt`** - Identical
- ✅ **`StatisticsViewState.kt`** - Identical
- ✅ **`StatisticsViewStateMapper.kt`** - Identical
- ✅ **`StatisticsInteractor.kt`** - Identical
- ✅ **`StatisticsModule.kt`** - Identical
- ✅ **`components/StatisticCardComponent.kt`** - Very similar
- ✅ **`components/StatisticsHeaderComponent.kt`** - Very similar
- ✅ **`contents/StatisticsContentHolder.kt`** - Very similar
- ✅ **`contents/StatisticsErrorContent.kt`** - Very similar
- ✅ **`contents/StatisticsFatalErrorContent.kt`** - Very similar
- ✅ **`contents/StatisticsNominalContent.kt`** - Very similar
- ✅ **`contents/StatisticsNoSessionsContent.kt`** - Very similar
- ✅ **`contents/StatisticsNoUsersContent.kt`** - Very similar
- ✅ **`dialogs/StatisticsSelectUserDialog.kt`** - Very similar

### 6.2 Different Implementations

#### StatisticsScreen.kt
**Mobile:**
- `UiArrangement` parameter
- Has `StatisticsTopAppBar` in vertical mode
- Conditional sidebar

**TV:**
- No arrangement concept
- No top app bar
- Always has sidebar via `NavigationDrawer`

#### StatisticsTopAppBar
**Mobile Only:**
- Top app bar with navigation and user selection
- Not present in TV

---

## 7. App-Level Configuration

### 7.1 Build Configuration Differences

**Mobile (`android/mobile/app/build.gradle.kts`):**
- Plugin: `simplehiit.android.application.handheld`
- Depends on: `androidx.compose.adaptive`
- Module graph assertions for mobile-specific rules

**TV (`android/tv/app/build.gradle.kts`):**
- Plugin: `simplehiit.android.application.tv`
- Depends on: `androidx.tv.foundation`, `androidx.tv.material`
- Module graph assertions for TV-specific rules

### 7.2 Navigation Structure

**Mobile:**
- Uses `androidx.navigation3` with conditional layouts
- Top bar OR sidebar based on screen size
- `UiArrangement` enum drives layout decisions

**TV:**
- Uses `androidx.navigation3` with `NavigationDrawer`
- Always sidebar in drawer
- D-pad focused navigation

---

## 8. Implementation Patterns Analysis

### 8.1 Architecture Patterns (Identical)
Both platforms follow the same clean architecture:
- ✅ **ViewModel**: Manages UI state and business logic coordination
- ✅ **ViewState**: Sealed classes for UI states (Loading, Error, Nominal, etc.)
- ✅ **ViewStateMapper**: Maps domain models to view states
- ✅ **Interactor**: Coordinates use cases from domain layer
- ✅ **Screen composable**: Entry point collecting ViewModels
- ✅ **ContentHolder**: Switches content based on ViewState
- ✅ **State-specific content**: Separate composables for each state
- ✅ **Reusable components**: Small, focused UI components

### 8.2 State Management (Identical)
- ✅ Both use `collectAsStateWithLifecycle()`
- ✅ Both use `StateFlow` in ViewModels
- ✅ Both use `SharingStarted.WhileSubscribed(5000)`
- ✅ Same dialog state management pattern

### 8.3 Dependency Injection (Identical)
- ✅ Both use Hilt with `@HiltViewModel`
- ✅ Same module structure in `di/` packages
- ✅ Same injection patterns

---

## 9. Key Discrepancies Summary

### 9.1 Feature Discrepancies

| Feature | Mobile | TV | Can Share? |
|---------|--------|-----|------------|
| **Adaptive Layouts** | ✅ UiArrangement | ❌ Fixed layout | Partial |
| **Top Navigation Bar** | ✅ Vertical mode | ❌ | No |
| **Navigation Drawer** | ❌ | ✅ | No |
| **Window Size Classes** | ✅ | ❌ | No |
| **Dialog Button Layout** | Adaptive | Fixed | No |
| **Loading Animation** | Simple | Complex | Design choice |
| **Focus Management** | Click-based | D-pad focused | No |

### 9.2 Implementation Inconsistencies

#### Component Library Divergence
- **Mobile:** Uses `androidx.compose.material3`
- **TV:** Uses `androidx.tv.material3`
- **Impact:** Different component APIs require separate implementations

#### Dialog Systems
- **Mobile:**
  - Uses `Dialog` with `OutlinedTextField`
  - `AdaptiveDialogButtonsLayout` for responsive buttons
  - Individual dialog files for each type

- **TV:**
  - Uses `DialogContentLayout` wrapper
  - `BasicTextField` with custom decoration
  - Reusable dialog components (`DialogInput`, `RadioButtonsDialog`)

#### Button Components
- **Mobile:** Uses Material3 buttons directly (`Button`, `TextButton`, etc.) + `ToggleButton`
- **TV:** Custom button components (`ButtonFilled`, `ButtonBordered`, `ButtonText`, etc.)

#### Theme Structure
- **Mobile:** `MobileDarkColorScheme`, `MobileLightColorScheme`, `MobileType`
- **TV:** `TvDarkColorScheme`, `TvLightColorScheme`, `TvType`, `TvShapes`

---

## 10. Opportunities for Code Sharing

### 10.1 High Priority - Immediately Shareable (95-100% identical)

These can be moved to a shared module with minimal changes:

#### ViewModels (All identical)
- `HomeViewModel.kt`
- `SessionViewModel.kt`
- `SettingsViewModel.kt`
- `StatisticsViewModel.kt`

#### ViewStates (All identical)
- `HomeViewState.kt`
- `SessionViewState.kt`
- `SettingsViewState.kt`
- `StatisticsViewState.kt`

#### ViewStateMappers (All identical)
- `HomeViewStateMapper.kt`
- `SessionViewStateMapper.kt`
- `SettingsViewStateMapper.kt`
- `StatisticsViewStateMapper.kt`

#### Interactors (All identical)
- `HomeInteractor.kt`
- `SessionInteractor.kt`
- `SettingsInteractor.kt`
- `StatisticsInteractor.kt`

#### Hilt Modules (All identical)
- `HomeModule.kt`
- `SessionModule.kt`
- `SettingsModule.kt`
- `StatisticsModule.kt`

**Note:** These are 100% platform-agnostic and contain no Android framework dependencies beyond standard Kotlin/Coroutines.

### 10.2 Medium Priority - Shareable with Abstraction (70-90% similar)

These need a platform abstraction layer:

#### Content Composables
Most `contents/` composables are 70-90% identical:
- `HomeContentHolder.kt` - Parameter differences only
- `HomeErrorContent.kt` - Different Material imports
- `HomeMissingUsersContent.kt` - Different Material imports
- `HomeNominalContent.kt` - Different Material imports
- (Same pattern for Session, Settings, Statistics)

**Strategy:** Create shared composables with platform-specific Material component injection.

#### Components
Many `components/` are very similar:
- `NumberCyclesComponent.kt`
- `SingleUserHeaderComponent.kt`
- `ExerciseDescriptionComponent.kt`
- `ExerciseDisplayComponent.kt`
- `CountDownComponent.kt`
- `RemainingPercentageComponent.kt`
- `StatisticCardComponent.kt`
- `StatisticsHeaderComponent.kt`

**Strategy:** Extract business logic, use platform-specific UI primitives.

### 10.3 Low Priority - Platform-Specific

These should remain platform-specific:

#### Navigation & Layout
- `HomeScreen.kt` - Different navigation patterns
- `SessionScreen.kt` - Different navigation patterns
- `SettingsScreen.kt` - Different navigation patterns
- `StatisticsScreen.kt` - Different navigation patterns
- `UiArrangement.kt` - Mobile only
- `NavigationSideBar.kt` - Different interaction models
- `NavigateUpTopBar.kt` - Mobile only
- `StatisticsTopAppBar.kt` - Mobile only

#### Dialogs
- Input dialogs (different TextField implementations)
- Dialog button layouts (adaptive vs fixed)
- Radio button dialogs

#### Theme & Styling
- Color schemes
- Typography
- Shapes
- Material theme wrappers

#### Platform-Specific Components
- Mobile: `AdaptiveDialogButtonsLayout`, Window size helpers
- TV: Custom buttons, focus management, NavigationDrawer integration

---

## 11. Recommended Shared-UI Module Structure

```
shared-ui/
├── viewmodels/
│   ├── HomeViewModel.kt
│   ├── SessionViewModel.kt
│   ├── SettingsViewModel.kt
│   └── StatisticsViewModel.kt
├── viewstates/
│   ├── HomeViewState.kt
│   ├── SessionViewState.kt
│   ├── SettingsViewState.kt
│   └── StatisticsViewState.kt
├── mappers/
│   ├── HomeViewStateMapper.kt
│   ├── SessionViewStateMapper.kt
│   ├── SettingsViewStateMapper.kt
│   └── StatisticsViewStateMapper.kt
├── interactors/
│   ├── HomeInteractor.kt
│   ├── SessionInteractor.kt
│   ├── SettingsInteractor.kt
│   └── StatisticsInteractor.kt
├── di/ (if not keeping in platform modules)
│   ├── HomeModule.kt
│   ├── SessionModule.kt
│   ├── SettingsModule.kt
│   └── StatisticsModule.kt
└── components/ (with platform abstraction)
    ├── NumberCyclesComponent.kt
    ├── ExerciseDisplayComponent.kt
    └── ... (other shareable components)
```

### Module Dependencies
```
shared-ui/
├── depends on:
│   ├── domain.common
│   ├── commonUtils
│   └── commonResources (for strings, dimensions)
└── independent from:
    ├── android.common (no Android framework)
    ├── androidx.compose.* (no Compose)
    └── androidx.hilt.* (Hilt OK, but keep modules in platforms)
```

---

## 12. Migration Strategy

### Phase 1: Extract Pure Logic (Low Risk)
1. Create `shared-ui` module
2. Move ViewModels, ViewStates, Mappers, Interactors
3. Update imports in mobile and TV
4. **Effort:** 1-2 days
5. **Risk:** Very low (100% code reuse)

### Phase 2: Abstract Common Components (Medium Risk)
1. Create platform abstraction for Material components
2. Migrate content composables with platform injection
3. Migrate simple components
4. **Effort:** 1-2 weeks
5. **Risk:** Medium (requires testing on both platforms)

### Phase 3: Evaluate Dialog Unification (High Risk)
1. Analyze if dialog unification is worthwhile
2. Might be better to keep separate given different UX paradigms
3. **Decision:** Recommend keeping separate

### Phase 4: Extract Common UI Logic (Optional)
1. Extract non-UI logic from components
2. Keep UI rendering platform-specific
3. Share validation, calculations, formatting
4. **Effort:** Ongoing as needed

---

## 13. Constraints & Considerations

### 13.1 Platform UX Differences
- **Mobile:** Touch-based, varied screen sizes, portrait/landscape
- **TV:** D-pad/remote-based, large screen, landscape only, 10-foot UI
- **Implication:** Some UI divergence is by design, not inconsistency

### 13.2 Material Library Differences
- `androidx.compose.material3` vs `androidx.tv.material3`
- Different component APIs even for similar concepts
- **Implication:** Direct component sharing is difficult

### 13.3 Focus Management
- TV requires extensive focus management for D-pad navigation
- Mobile uses touch/click model
- **Implication:** Interaction handling must remain separate

### 13.4 Hilt Modules
- Could be shared, but keeping them in platform modules allows platform-specific bindings if needed
- **Recommendation:** Keep in platform modules initially, consider sharing later

---

## 14. Testing Impact

### Current State
Both platforms have parallel test structures. Sharing code will:
- ✅ Reduce test duplication for ViewModels
- ✅ Reduce test duplication for Mappers
- ✅ Reduce test duplication for Interactors
- ⚠️ Require testing shared components on both platforms

### Test Strategy for Shared Module
1. **Unit tests** in `shared-ui/src/test/` for logic
2. **Platform-specific UI tests** remain in mobile and TV modules
3. **Snapshot tests** for shared components on both platforms

---

## 15. Conclusion

### Summary of Findings

**Identical Code (100% shareable):**
- All ViewModels (4 files)
- All ViewStates (4 files)
- All ViewStateMappers (4 files)
- All Interactors (4 files)
- All Hilt modules (4 files)
- **Total: ~20 files, ~2000+ lines of identical code**

**Very Similar Code (70-90% shareable with abstraction):**
- Content holders and content composables (~20 files)
- Most UI components (~15 files)
- **Total: ~35 files, ~4000+ lines with potential for sharing**

**Platform-Specific (should remain separate):**
- Screen entry points (navigation integration)
- Dialogs (different UX paradigms)
- Navigation components
- Theme and styling
- Platform-specific helpers (UiArrangement, focus management)
- **Total: ~40 files, ~5000+ lines appropriately different**

### Key Recommendations

1. **Immediate action:** Extract all ViewModels, ViewStates, Mappers, and Interactors to `shared-ui` module
2. **Short-term:** Create abstraction layer for Material components and migrate content composables
3. **Long-term:** Continuously evaluate component sharing opportunities
4. **Keep separate:** Dialogs, navigation, themes, and platform-specific UX patterns

### Expected Benefits

- **Code reduction:** ~40-50% reduction in duplicated business logic
- **Maintenance:** Single source of truth for view state management
- **Consistency:** Guaranteed behavioral consistency across platforms
- **Development speed:** Changes to business logic propagate automatically
- **Testing:** Reduced test duplication for logic layer

### Risks to Mitigate

- **Abstraction complexity:** Don't over-abstract; keep it simple
- **Platform regressions:** Thorough testing on both platforms after migration
- **Build complexity:** Ensure clean module boundaries
- **Team coordination:** Changes to shared code affect both platforms

---

## Appendix A: Detailed File Comparison

### Files with 100% Identical Content

#### Home Module
- `HomeViewModel.kt` (98% - 2 comment lines differ)
- `HomeViewState.kt` (100%)
- `HomeViewStateMapper.kt` (100%)
- `HomeInteractor.kt` (100%)
- `HomeModule.kt` (100%)

#### Session Module
- `SessionViewModel.kt` (100%)
- `SessionViewState.kt` (100%)
- `SessionViewStateMapper.kt` (100%)
- `SessionInteractor.kt` (100%)
- `SessionModule.kt` (100%)

#### Settings Module
- `SettingsViewModel.kt` (100%)
- `SettingsViewState.kt` (100%)
- `SettingsViewStateMapper.kt` (100%)
- `SettingsInteractor.kt` (100%)
- `SettingsModule.kt` (100%)

#### Statistics Module
- `StatisticsViewModel.kt` (100%)
- `StatisticsViewState.kt` (100%)
- `StatisticsViewStateMapper.kt` (100%)
- `StatisticsInteractor.kt` (100%)
- `StatisticsModule.kt` (100%)

### Files with 70-90% Similar Content

#### Components
- `NumberCyclesComponent.kt` - ~85% (different Material imports)
- `SingleUserHeaderComponent.kt` - ~85%
- `ExerciseDescriptionComponent.kt` - ~90%
- `ExerciseDisplayComponent.kt` - ~85%
- `CountDownComponent.kt` - ~90%
- `RemainingPercentageComponent.kt` - ~90%
- `RunningSessionStepInfoDisplayComponent.kt` - ~85%
- `StatisticCardComponent.kt` - ~90%
- `StatisticsHeaderComponent.kt` - ~85%
- `SettingsExercisesSelectedComponent.kt` - ~85%
- `SettingsFieldComponent.kt` - ~80%
- `SettingsToggleComponent.kt` - ~75%
- `SettingsUsersComponent.kt` - ~80%

#### Content Holders
- All `ContentHolder.kt` files - ~85%
- All content state files - ~80-90%

---

**Document Version:** 1.0
**Date:** December 26, 2025
**Author:** Cline (AI Assistant)
**Purpose:** Inform shared-ui module creation strategy
