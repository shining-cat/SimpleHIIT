# SimpleHIIT ToDo list

## Missing features / issues
* had to exclude the external instrumented tests module from report aggregation plugin, see testAggregation block in build.gradle. [discussion ongoing with author...](https://github.com/gmazzo/gradle-android-test-aggregation-plugin/issues/32)
* check what this flooding error is and fix if possible: _Attempt to update InputPolicyFlags without permission ACCESS_SURFACE_FLINGER_
* find a way to fix resolution issue when adding `id("com.google.dagger.hilt.android")` to `libraries_gradle_config`, to remove it from every module and apply it from the plugin
* beep sound playback for countdown is not very well synced with timer. Check [audio latency](https://developer.android.com/ndk/guides/audio/audio-latency) [check this example](https://github.com/o4oren/android-kotlin-metronome/blob/master/app/src/main/java/geva/oren/android_kotlin_metronome/services/MetronomeService.kt), using [Soundpool](https://developer.android.com/reference/android/media/SoundPool?hl=en)
* SessionErrorStateContent is empty
* replace toggle buttons' design with the one with a toggle check from Material, to make it more clear for the user
* design for statistics needs some love
* design for session summary needs some love
* create a About section, in which to add credits for PoseMy.Art

## Code refactoring: layouts
* Fix layouts composition:
  * there are nested columns that seem redundant between screens and content composables. Check and remove accordingly
  * the fact that settings screen is scrollable is probably what messes up the position of its loading indicator
* fix broken layout in landscape

## Code refactoring: architecture
* check this about [replacing sealed classes with interfaces](https://jorgecastillo.dev/sealed-interfaces-kotlin)
* check [this about reducing amount of code](https://kotlinlang.org/docs/fun-interfaces.html#sam-conversions), using [the invoke operator](https://chrynan.codes/invoking-usecases-the-kotlin-way/)
* wrap usecases in _interactors_ objects to reduce number of parameters in `viewmodels`' constructors

## Assets production
* refine statistics cards design and find/create icons for each
  * longest streak: icon of a cup and a calendar showing checked days
  * current streak: icon of a calendar showing checked days - IF current streak == longest, switch to same icon as longest streak to make it more clear
  * average session length: icon of the app above a clock
  * total time : icon of a clock
  * average session count per week: icon of the app above a calendar
  * total sessions count: laurels crown

## General technical improvements
* see [moving from kapt to ksp](https://developer.android.com/build/migrate-to-ksp)
* see [moving from dsl to toml](https://developer.android.com/build/migrate-to-catalogs)
* check out `remember` for state in composables and implement
* write tests on `Viewmodels`
* fix test coverage task for instrumented tests not reporting any coverage. use dedicated simplified project jacoco_exp to investigate
* switch to [version catalog for gradle dependencies](https://proandroiddev.com/mastering-gradle-dependency-management-with-version-catalogs-a-comprehensive-guide-d60e2fd1dac2)
* check out [this article about including the inter-modules dependencies graph generation to the CI](https://medium.com/google-developer-experts/how-to-display-your-android-project-dependency-graph-in-your-readme-file-e52dcadafa7a)
* CI github actions: run tests + linter (KTlint) before merge,[see article](https://medium.com/geekculture/how-to-build-sign-and-publish-android-application-using-github-actions-aa6346679254) or[ this one](https://proandroiddev.com/create-android-release-using-github-actions-c052006f6b0b?source=rss----c72404660798---4)

## Form factors (phone - AndroidTV - smartWatch)
* check [Google sample for Watch](https://github.com/android/wear-os-samples/tree/main/WearVerifyRemoteApp)
* form factor UX differences: phone should maybe not offer multi-users?

## Miscellaneous / nice to have
* instead of a dialog for the total number of repetitions see if a + and - buttons wouldn't be better
* add total length of session below the number of repetitions so user knows how long it will last
* when pausing running session, the gif behind the dialog keeps moving... find a way to freeze this if possible
* translate to FR and SV. Maybe add language selection in settings to be able to demo it?
* we follow system dark/light theme switch, maybe we could add a choice in settings to let user decide? (follow system (would be default), force dark, force light)
* add home screen shortcut launchers for start session and statistics
* Nice to have and study case: [particles animation](https://proandroiddev.com/creating-a-particle-explosion-animation-in-jetpack-compose-4ee42022bbfa)
* [performance study case](https://proandroiddev.com/jetpack-compose-tutorial-improving-performance-in-dribbble-audio-app-b19848cf12e3)

## Discuss with others
* How to handle building the different app form factors, maybe[ flavours and buildtypes?](https://blog.protein.tech/product-flavors-and-build-types-in-android-projects-customizing-base-urls-logos-and-more-bf0099508949?source=rss------android_development-5)

