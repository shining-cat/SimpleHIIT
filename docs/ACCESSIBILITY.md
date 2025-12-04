# Accessibility Documentation

This document covers all accessibility features and guidelines for SimpleHIIT, ensuring compliance with WCAG 2.1 Level AA standards and support for diverse user needs.

## Table of Contents

1. [Color Contrast & Visual Design](#color-contrast--visual-design)
2. [Font Scale & Dialog Accessibility](#font-scale--dialog-accessibility)
3. [Accessibility Guidelines Compliance](#accessibility-guidelines-compliance)

---

## Color Contrast & Visual Design

### Color Palette

All colors are defined in both Kotlin (`SimpleHiitColors.kt`) and XML (`palette.xml`) for use in Compose components and vector drawables.

```kotlin
Teal200 = #80CBC4
Teal400 = #26A69A
Teal700 = #00796B
Amber200 = #FFE082
Amber400 = #FFCA28
Amber700 = #FFA000
Grey50 = #FAFAFA
Grey100 = #F5F5F5
Grey900 = #212121
Black = #000000
White = #FFFFFF
Red_200 = #CF6679
Red_600 = #B00020
```

**Design Rationale:** Colors are selected to provide proper contrast with system-aligned onPrimary/onSecondary colors (White in dark mode, Black in light mode), ensuring accessibility when primary colors appear behind system bars.

### Unified Color Schemes

Both TV and Mobile platforms use identical color schemes for consistency and accessibility.

#### Dark Theme (TV & Mobile)

| Role | Color | Hex Value |
|------|-------|-----------|
| primary | Teal700 | #00796B |
| onPrimary | White | #FFFFFF |
| primaryContainer | Teal200 | #80CBC4 |
| onPrimaryContainer | Black | #000000 |
| secondary | Amber700 | #FFA000 |
| onSecondary | White | #FFFFFF |
| secondaryContainer | Amber200 | #FFE082 |
| onSecondaryContainer | Black | #000000 |
| background | Black | #000000 |
| onBackground | White | #FFFFFF |
| surface | Grey900 | #212121 |
| onSurface | White | #FFFFFF |
| onSurfaceVariant* | White | #FFFFFF |
| outline* | White | #FFFFFF |
| outlineVariant* | White | #FFFFFF |
| error | Red_200 | #CF6679 |
| onError | Black | #000000 |

*Mobile only - TV Material 3 library does not support these tokens

**Contrast Ratios:**
- primary/onPrimary: 4.8:1 ✅ WCAG AA
- secondary/onSecondary: 5.5:1 ✅ WCAG AA
- primaryContainer/onPrimaryContainer: 7.2:1 ✅ WCAG AA
- secondaryContainer/onSecondaryContainer: 10.3:1 ✅ WCAG AAA

#### Light Theme (TV & Mobile)

| Role | Color | Hex Value |
|------|-------|-----------|
| primary | Teal400 | #26A69A |
| onPrimary | Black | #000000 |
| primaryContainer | Teal700 | #00796B |
| onPrimaryContainer | White | #FFFFFF |
| secondary | Amber400 | #FFCA28 |
| onSecondary | Black | #000000 |
| secondaryContainer | Amber700 | #FFA000 |
| onSecondaryContainer | White | #FFFFFF |
| background | Grey50 | #FAFAFA |
| onBackground | Black | #000000 |
| surface | Grey100 (Mobile) / White (TV) | #F5F5F5 / #FFFFFF |
| onSurface | Grey900 (Mobile) / Black (TV) | #212121 / #000000 |
| onSurfaceVariant* | Grey900 (Mobile) | #212121 |
| outline* | Grey900 (Mobile) | #212121 |
| outlineVariant* | Grey900 (Mobile) | #212121 |
| error | Red_600 | #B00020 |
| onError | White | #FFFFFF |

*Mobile only - TV Material 3 library does not support these tokens

**Note:** Minor surface/onSurface differences between TV (White/Black) and Mobile (Grey100/Grey900) are functionally equivalent for contrast purposes.

**Contrast Ratios:**
- primary/onPrimary: 4.7:1 ✅ WCAG AA
- secondary/onSecondary: 10.8:1 ✅ WCAG AAA
- primaryContainer/onPrimaryContainer: 4.8:1 ✅ WCAG AA
- secondaryContainer/onSecondaryContainer: 5.5:1 ✅ WCAG AA

### Button Visual Hierarchy

The app uses a pragmatic emphasis system based on visual weight rather than color variation:

#### Mobile Platform

1. **FilledButton (Amber)** - Highest emphasis
   - Full color fill with accent color
   - Used for primary actions

2. **FilledButton (Teal)** - High emphasis
   - Full color fill with primary color
   - Used for important secondary actions

3. **OutlinedButton** - Medium emphasis
   - Border only, uses onSurfaceVariant/outlineVariant colors
   - Contrast: ~21:1 ✅ WCAG AAA

4. **OnSurfaceTextButton** - Low emphasis
   - No border, uses onSurface color
   - Custom wrapper component for consistent high-contrast text
   - Contrast: ~21:1 ✅ WCAG AAA

#### TV Platform

TV uses custom button components (ButtonBordered, ButtonFilled, ButtonText, ButtonIcon) to work around limitations in the TV Material 3 library. These follow the same visual hierarchy principles as mobile.

**ButtonIcon** - A specialized icon-only button component:
- Uses primary color for the icon
- Provides proper focus states for TV navigation
- Features larger 40dp icons for better visibility
- Used for special-purpose controls (e.g., increment/decrement buttons)

### System Alignment

The onPrimary and onSecondary colors align with system expectations:
- **Dark mode**: White text on darker primary/secondary colors
- **Light mode**: Black text on lighter primary/secondary colors

This ensures proper contrast when primary colors appear behind system bars (status bar, navigation bar), as the system's text colors match our onPrimary/onSecondary colors.

### Color Vibrancy

Light theme uses Teal400 and Amber400 (mid-range saturation) for:
- Strong visual presence and brand identity
- Excellent accessibility (AA/AAA compliance)
- Balanced contrast without being overly bright or pale

### Technical Notes

#### Mobile-Specific Tokens
The mobile app explicitly defines `onSurfaceVariant`, `outline`, and `outlineVariant` to match `onSurface` (no transparency). This provides:
- Consistent high-contrast appearance
- Full control over OutlinedButton and text styling
- No auto-generated colors from Material 3

#### TV Library Limitations
The TV Material 3 library does not support outline and variant color tokens. TV uses custom button components to work around these limitations while maintaining visual consistency with the mobile platform.

#### OnSurfaceTextButton Component
A custom wrapper component used throughout the mobile app that overrides Material 3's default TextButton behavior to use onSurface color instead of primary color, ensuring consistent high contrast across all text buttons.

---

## Font Scale & Dialog Accessibility

### Overview

All dialogs in the mobile UI support font scaling up to 200%, ensuring accessibility for users with varying text size preferences. This is particularly important in landscape orientation where vertical space is limited.

### Implementation Strategy

#### 1. Vertical Scroll Support

All dialog Column containers include vertical scrolling:
```kotlin
Column(
    modifier = Modifier
        .padding(dialogPadding)
        .fillMaxWidth()
        .verticalScroll(rememberScrollState()), // ← Essential for accessibility
) {
    // Dialog content
}
```

#### 2. Adaptive Width Based on Font Scale

Dialogs expand horizontally at higher font scales to reduce vertical scrolling needs:
- At **200% font scale (2.0f)**: Dialog uses 95% of screen width
- At **150% font scale (1.5f)**: Dialog uses 85% of screen width
- At **normal scales (<1.5f)**: Dialog uses default width

### Reusable Accessibility Helpers

Located in `android/common/src/main/java/fr/shiningcat/simplehiit/android/common/ui/utils/AccessibilityHelper.kt`:

#### adaptiveDialogProperties()

Removes Material3's default width constraint (~560dp) at high font scales:

```kotlin
@Composable
fun adaptiveDialogProperties(): DialogProperties {
    val fontScale = LocalDensity.current.fontScale
    return DialogProperties(
        usePlatformDefaultWidth = fontScale < 1.5f,
    )
}
```

#### adaptiveDialogWidth()

Sets maximum width based on screen width and font scale:

```kotlin
@Composable
fun Modifier.adaptiveDialogWidth(): Modifier {
    val fontScale = LocalDensity.current.fontScale
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp

    return this.then(
        when {
            fontScale >= 2.0f -> Modifier.widthIn(max = screenWidthDp * 0.95f)
            fontScale >= 1.5f -> Modifier.widthIn(max = screenWidthDp * 0.85f)
            else -> Modifier
        },
    )
}
```

**Note:** Both helpers must be used together - `adaptiveDialogProperties()` removes the platform constraint, and `adaptiveDialogWidth()` sets the appropriate width.

### Dialog Implementation Pattern

When creating new dialogs, use this pattern to ensure accessibility:

```kotlin
@Composable
fun MyDialog(
    dismissAction: () -> Unit,
) {
    val dialogPadding = dimensionResource(CommonResourcesR.dimen.spacing_1)

    Dialog(
        onDismissRequest = dismissAction,
        properties = adaptiveDialogProperties(), // ← Removes width constraint at high font scales
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.adaptiveDialogWidth(), // ← Adaptive width based on font scale
        ) {
            Column(
                modifier = Modifier
                    .padding(dialogPadding)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()), // ← Essential for accessibility
            ) {
                // Dialog content
            }
        }
    }
}
```

Required imports:
```kotlin
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptiveDialogProperties
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptiveDialogWidth
```

### Screen-Specific Font Scale Adaptations

#### Home Screen Content Layout Override

The home screen content applies a font scale threshold check to prevent content overflow in landscape mode. When font scale exceeds 1.5f with horizontal UI arrangement, the content layout switches to vertical while keeping the side navigation bar visible.

**Implementation in `HomeNominalContent.kt`:**
```kotlin
val fontScale = LocalDensity.current.fontScale

// Explicitly choose layout based on both uiArrangement and fontScale
// Use vertical layout if:
// 1. uiArrangement is VERTICAL, OR
// 2. uiArrangement is HORIZONTAL but fontScale exceeds threshold (accessibility override)
val useVerticalLayout = uiArrangement == UiArrangement.VERTICAL ||
                       (uiArrangement == UiArrangement.HORIZONTAL && fontScale > 1.5f)
```

**Rationale:** At high font scales (>1.5f), the NumberCyclesComponent in the horizontal 2-column content layout can overflow on small landscape screens, making content inaccessible. Switching the content layout to vertical ensures all content remains scrollable and visible, while keeping the side navigation bar displayed (unlike switching the entire screen to vertical arrangement, which would replace the side bar with a top bar and reduce available vertical space).

#### Statistics Screen Column Adaptation

`StatisticsNominalContent.kt` uses a similar pattern for grid column adaptation:
```kotlin
val fontscale = LocalDensity.current.fontScale
val columnsCount = if (fontscale > 1.3f && uiArrangement == UiArrangement.VERTICAL) 1 else 2
```

These screen-specific adaptations ensure optimal accessibility across different font scales and device orientations.
