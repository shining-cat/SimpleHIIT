# SimpleHIIT ToDo list

## Missing features / issues
* input dialog should autofocus on input field, with caret after the last character, and open keyboard
* user selection in phone vertical could be a flowrow rather than a grid, makes more sense when we have other content below
* TV: text button focus is not visible enough when button is on a surface (like in a dialog) because
  container focused color for textbutton is surface
* session running screen issue when it does not fit vertically on mobile phone screen... should it
  scroll or be constrained?
* improve 200% fontscale layouts

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
* check [Google sample for Watch](https://github.com/android/wear-os-samples/tree/main/WearVerifyRemoteApp)
* move the whole thing to KMP and build other platforms

## Miscellaneous / nice to have
* When user unselect ALL exercise types, allow the session to still run, without showing any
  exercise, as a timer only. Show a message instead of the missing gifs.
* create a _About_ section, in which to add credits for PoseMy.Art, it could also hold the
  hiit_description.
* refine fr and swedish translations
* statistics maybe remove seconds from displays to reduce clutter?
* create an onboarding feature, including the user creation then selection and the multiple cycles
  mechanics
* when pausing running session, the gif behind the dialog keeps moving... find a way to freeze this
  if possible
* we follow system dark/light theme switch, maybe we could add a choice in settings to let user
  decide? (follow system (would be default), force dark, force light)
* add home screen shortcut launchers for start session and statistics
* Nice to have and study
  case: [particles animation](https://proandroiddev.com/creating-a-particle-explosion-animation-in-jetpack-compose-4ee42022bbfa)

