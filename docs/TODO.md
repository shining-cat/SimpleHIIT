# SimpleHIIT ToDo list

## Missing features / issues
* have to press back button twice to exit running session on tv?
*  tv pause dialog needs aligning with mobile
* text objects do not wrap words properly, to fix proper hyphenation, we need to set the proper option and ensure the locale is matching the language selected in prefs
* improve layouts, including 200% fontscale, orientations and devices:
  * [x] home
  * [x] settings
  * [x] statistics
  * [ ] session (this one might be tricky as it's a screen we don't want the user to need to scroll, they'll be busy exercising)

## Publication
* Find how to publish: Fdroid, or github, or home?

## General technical improvements
* windowSizeClass.windowWidthSizeClass is deprecated, maybe combine fix with navigation 3 which should impact adaptive navigation
* upgrade to androidx.navigation3 include new adaptive navigation
  see https://medium.com/proandroiddev/future-of-android-why-navigation-3-is-a-game-changer-f835f841c17f
* use new [compose stability plugin](https://proandroiddev.com/compose-stability-analyzer-real-time-stability-insights-for-jetpack-compose-1399924a0a64) to review composables and eventually optimise them further (also gradle plugin)
* could we extract even more **platform-agnostic** logic from the viewmodels?
* add tests on viewmodels
* screenshot tests if they can run on github for free
* automation to warn about deprecated objects use running regularly on CI?

## CI/Github actions
* See automation of build scripts verification:
  github.com/gradle/gradle-enterprise-build-validation-scripts. There should be a few free tools

## Form factors (phone - AndroidTV - smartWatch)
* move the whole thing to KMP and build other platforms
* watch? check [Google sample for Watch](https://github.com/android/wear-os-samples/tree/main/WearVerifyRemoteApp)

## Miscellaneous / nice to have
* when pausing running session, the gif behind the dialog keeps moving... find a way to freeze this
  if possible
* we follow system dark/light theme switch, maybe we could add a choice in settings to let user
  decide? (follow system (would be default), force dark, force light)
* user object could expose the timestamp of their last session so we could sort them on this on the home for convenience
* When user unselect ALL exercise types, allow the session to still run, without showing any
  exercise, as a timer only. Show a message instead of the missing gifs.
* create a _About_ section, in which to add credits for PoseMy.Art, it could also hold the
  hiit_description, accessible through a new ? button in the home top app bar actions menu
* refine fr and swedish translations
* statistics maybe remove seconds from displays to reduce clutter?
