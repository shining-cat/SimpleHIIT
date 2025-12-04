# SimpleHIIT ToDo list

## Missing features / issues
* home screen's cycle count selector in landscape and large fonts doesn't show buttons!
* improve ui arrangement bucketting: very large displays in portrait are rendered as horizontal and it doesn't look very good (wait for migration to navigation 3, might impact this)

## Publication
* Find how to publish: Fdroid, or github, or home?

## General technical improvements
* windowSizeClass.windowWidthSizeClass is deprecated, maybe combine fix with navigation 3 which should impact adaptive navigation
* upgrade to androidx.navigation3 include new adaptive navigation
  see https://medium.com/proandroiddev/future-of-android-why-navigation-3-is-a-game-changer-f835f841c17f
* use new [compose stability plugin](https://proandroiddev.com/compose-stability-analyzer-real-time-stability-insights-for-jetpack-compose-1399924a0a64) to review composables and eventually optimise them further (also gradle plugin)
* could we extract even more **platform-agnostic** logic from the viewmodels?
* add tests on viewmodels after having tried to make them lighter
* screenshot tests if they can run on github for free

## CI/Github actions
* See automation of build scripts verification:
  github.com/gradle/gradle-enterprise-build-validation-scripts. There should be a few free tools
* automation to warn about deprecated objects use running regularly on CI?

## Form factors (phone - AndroidTV - smartWatch)
* move the whole thing to KMP and build other platforms
* watch? check [Google sample for Watch](https://github.com/android/wear-os-samples/tree/main/WearVerifyRemoteApp)

## Miscellaneous / nice to have
* we follow system dark/light theme switch, maybe we could add a choice in settings to let user
  decide? (follow system (would be default), force dark, force light)
* user object could expose the timestamp of their last session so we could sort them on this on the home for convenience
* When user unselect ALL exercise types, allow the session to still run, without showing any
  exercise, as a timer only. Show a message instead of the missing gifs.
* create a _About_ section, in which to add credits for PoseMy.Art, it could also hold the
  hiit_description, accessible through a new ? button in the home top app bar actions menu
* refine fr and swedish translations
* statistics maybe remove seconds from displays to reduce clutter?
