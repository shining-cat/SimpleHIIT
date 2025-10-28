# SimpleHIIT ToDo list

## Missing features / issues
* TV: text button focus is not visible enough when button is on a surface (like in a dialog) because
  container focused color for textbutton is surface
* improve layouts, including 200% fontscale, orientations and devices:
  * [x] home
  * [ ] statistics
  * [ ] settings
  * [ ] session (this one might be tricky as it's a screen we don't want the user to need to scroll, they'll be busy exercising)

## Publication
* Find how to publish on Fdroid?

## General technical improvements
* add tests on viewmodels
* screenshot tests if they can run on github for free
* ktlint and AS "format code" on commit contradict each other leading to CI failures if option is checked in AS
* upgrade to androidx.navigation3 (in alpha as of Aug.25) include new adaptive navigation
  see https://medium.com/proandroiddev/future-of-android-why-navigation-3-is-a-game-changer-f835f841c17f

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
