# SimpleHIIT ToDo list

## Missing features / issues
* beep sound playback for countdown is not very well synced with timer

## Code refactoring: layouts
* Fix layouts composition:
  * there are nested columns that seem redundant between screens and content composables. Check and remove accordingly
  * the fact that settings screen is scrollable is probably what messes up the position of its loading indicator
* fix broken layout in landscape

## Code refactoring: architecture
* split to modules. [See article about how to split](https://betterprogramming.pub/the-real-clean-architecture-in-android-modularization-e26940fd0a23?source=rss-8f0052074f18------2)
* once split into modules, add inter-modules dependencies graph generator plugin:
  * classpath "com.vanniktech:gradle-dependency-graph-generator-plugin:0.8.0"
  * apply plugin: "com.vanniktech.dependency.graph.generator"
* split off the statistics section as a feature module to experiment with optional feature management as android module
* check this about [replacing sealed classes with interfaces](https://jorgecastillo.dev/sealed-interfaces-kotlin)

## Assets production
* countdown beep sound
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
* check what this flooding error is and fix if possible: _Attempt to update InputPolicyFlags without permission ACCESS_SURFACE_FLINGER_
* check out remember for state in composables and implement
* write tests on Viewmodels, maybe extract some more logic out of them
* fix test coverage task for instrumented tests not reporting any coverage. use dedicated simplified project jacoco_exp
* switch to [version catalog for gradle dependencies](https://proandroiddev.com/mastering-gradle-dependency-management-with-version-catalogs-a-comprehensive-guide-d60e2fd1dac2)
* CI github actions: run tests + linter (KTlint) before merge,[see article](https://medium.com/geekculture/how-to-build-sign-and-publish-android-application-using-github-actions-aa6346679254) or[ this one](https://proandroiddev.com/create-android-release-using-github-actions-c052006f6b0b?source=rss----c72404660798---4)
* we follow system dark/light theme switch, maybe we could add a choice in settings to let user decide? (follow system (would be default), force dark, force light)
* add home screen shortcut launchers for start session and statistics

## Form factors (phone - AndroidTV - smartWatch)
* check [Google sample for Watch](https://github.com/android/wear-os-samples/tree/main/WearVerifyRemoteApp)
* form factor UX differences: phone should maybe not offer multi-users?

## Miscellaneous / nice to have
* translate to FR and SV. Maybe add language selection in settings to be able to demo it?
* Nice to have and study case: [particles animation](https://proandroiddev.com/creating-a-particle-explosion-animation-in-jetpack-compose-4ee42022bbfa)
* [performance study case](https://proandroiddev.com/jetpack-compose-tutorial-improving-performance-in-dribbble-audio-app-b19848cf12e3)
* check, see if interesting compared to github actions: [githooks to display warning on forgotten todos in commit, for example](https://betterprogramming.pub/want-to-avoid-forgotten-todos-in-your-project-lets-do-it-with-git-hooks-6a1835f26cf5?source=rss----d0b105d10f0a---4)

## Discuss with others
* How to handle building the different app form factors, maybe[ flavours and buildtypes?](https://blog.protein.tech/product-flavors-and-build-types-in-android-projects-customizing-base-urls-logos-and-more-bf0099508949?source=rss------android_development-5)
* See SimpleHiitDataStoreManagerImplTest -> how to trigger throwing exception from test dataStore?
* discuss filtering out CancellationException: how relevant is it to do it multiple times in a flow of data


