# SimpleHIIT ToDo list

## Code refactoring: layouts
* Fix layouts composition:
  * there are nested columns that seem redundant between screens and content composables. Check and remove accordingly
  * the fact that settings screen is scrollable is probably what messes up the position of its loading indicator
* fix broken layout in landscape

## Code refactoring: architecture
* split clean arch layers to modules to allow for future multiple form factor builds
* split off the statistics section as a feature module to experiment with optional feature management as android module
* if splitting into modules, add inter-modules dependencies graph generator plugin:
  * classpath "com.vanniktech:gradle-dependency-graph-generator-plugin:0.8.0"
  * apply plugin: "com.vanniktech.dependency.graph.generator"

## Assets production
* create GIFs for exercises with https://app.posemy.art/ (start by listing all the ones we have, then compose steps, probably 2 per exercises and export, then make gifs from that with Photoshop) _remember that exercises from the same family might often use the same base position_
* when making pictures for GIFS, insert watermark "@SimpleHIIT by Shining-cat" on body for each one
* refine statistics cards design and find icons for each
  * longest streak: icon of a cup and a calendar showing checked days
  * current streak: icon of a calendar showing checked days - IF current streak == longest, switch to same icon as longest streak to make it more clear
  * average session length: icon of the app above a clock
  * total time : icon of a clock
  * average session count per week: icon of the app above a calendar
  * total sessions count: laurels crown

## General technical improvements
* write tests on Viewmodels, maybe extract some more logic out of them
* fix test coverage task for instrumented tests not reporting any coverage. use dedicated simplified project jacoco_exp
* CI github actions: run tests + linter (KTlint) before merge,[see article](https://medium.com/geekculture/how-to-build-sign-and-publish-android-application-using-github-actions-aa6346679254)
* we follow system dark/light theme switch, maybe we could add a choice in settings to let user decide? (follow system (would be default), force dark, force light)
* add home screen shortcut launchers for start session and statistics

## Form factors (phone - AndroidTV - smartWatch)
* check [Google sample for Watch](https://github.com/android/wear-os-samples/tree/main/WearVerifyRemoteApp)
* form factor UX differences: phone should maybe not offer multi-users?

## Miscellaneous
* translate to FR and SV. Maybe add language selection in settings to be able to demo it?
* Nice to have and study case: [particles animation](https://proandroiddev.com/creating-a-particle-explosion-animation-in-jetpack-compose-4ee42022bbfa)
* [performance study case](https://proandroiddev.com/jetpack-compose-tutorial-improving-performance-in-dribbble-audio-app-b19848cf12e3)

## Discuss with others
* How to handle building the different app form factors
* See SimpleHiitDataStoreManagerImplTest -> how to trigger throwing exception from test dataStore?
* discuss filtering out CancellationException: how relevant is it to do it multiple times in a flow of data


