# SimpleHIIT ToDo list

## Missing features / issues
* exercise display in session running screen is messed up, components positions are all wrong
* text button focus is not visible enough when button is on a surface (like in a dialog) because container focused color for textbutton is surface
* Missing data extraction rules, see https://developer.android.com/about/versions/12/behavior-changes-12#backup-restore, and https://developer.android.com/guide/topics/data/autobackup, find how to set up backup_rules.xml and data_extraction_rules.xml in commonResources>src>main>res>xml
* Error when trying to upgrade the compose-BOM above 2023.06.xx:
  * Could not resolve all dependencies for configuration ':android:common:debugRuntimeClasspath'.
    Problems reading data from Binary store in /Users/shiva.bernhard@schibsted.com/.gradle/.tmp/gradle5795096560825037874.bin offset 188550 exists? true
    Problems loading the resolution results (0.0 secs). Read 346 values, last was: 5
    Corrupt serialized resolution result. Cannot find selected component (14958) for constraint releaseVariantReleaseRuntimePublication -> androidx.lifecycle:lifecycle-process:2.6.1

## Assets production
* redo app icon, remove name, set white background, only set the elapsed time on the clock as primary color (handle darkmode if possible?)
* refine statistics cards design and find/create icons for each
  * longest streak: icon of a cup and a calendar showing checked days
  * current streak: icon of a calendar showing checked days - IF current streak == longest, switch to same icon as longest streak to make it more clear
  * average session length: icon of the app above a clock
  * total time : icon of a clock
  * average session count per week: icon of the app above a calendar
  * total sessions count: laurels crown

## General technical improvements
* find a way to fix resolution issue when adding `id("com.google.dagger.hilt.android")` to `libraries_gradle_config`, to remove it from every module and apply it from the plugin
* had to exclude the external instrumented tests module from report aggregation plugin, see testAggregation block in build.gradle. [ongoing discussion with author...](https://github.com/gmazzo/gradle-android-test-aggregation-plugin/issues/32)
* fix test coverage task for instrumented tests not reporting any coverage. use dedicated simplified project jacoco_exp to investigate
* switch to [version catalog for gradle dependencies](https://proandroiddev.com/mastering-gradle-dependency-management-with-version-catalogs-a-comprehensive-guide-d60e2fd1dac2)
* BLOCKED: see [moving from kapt to ksp](https://developer.android.com/build/migrate-to-ksp), but check [first that HILT has moved to ksp](https://kotlinlang.org/docs/ksp-overview.html#resources)

## CI/Github actions
* check out [this article about including the inter-modules dependencies graph generation to the CI](https://medium.com/google-developer-experts/how-to-display-your-android-project-dependency-graph-in-your-ticke-file-e52dcadafa7a)
* CI github actions: run tests + linter (KTlint) before merge,[see article](https://medium.com/geekculture/how-to-build-sign-and-publish-android-application-using-github-actions-aa6346679254) or[ this one](https://proandroiddev.com/create-android-release-using-github-actions-c052006f6b0b?source=rss----c72404660798---4)

## Form factors (phone - AndroidTV - smartWatch)
* check [Google sample for Watch](https://github.com/android/wear-os-samples/tree/main/WearVerifyRemoteApp)
* see also [this article about multiplatform](https://proandroiddev.com/achieving-ios-compatibility-for-my-quotes-app-with-kotlin-edd364854a0d)
* also, compose is [now in alpha for AndroidTV](https://android-developers.googleblog.com/2023/05/building-pixel-perfect-living-room-experiences-compose-for-tv.html)
* see also [flavours and buildtypes?](https://blog.protein.tech/product-flavors-and-build-types-in-android-projects-customizing-base-urls-logos-and-more-bf0099508949?source=rss------android_development-5)

## Miscellaneous / nice to have
* integrate detekt / ktlint etc https://medium.com/@nagendran.p/integrating-detekt-in-the-android-studio-442128e971f8
* design for statistics needs some love, see assets creation
* create a _About_ section, in which to add credits for PoseMy.Art, it could also hold the hiit_description.
* translate to FR and SV. Maybe add language selection in settings to be able to demo it?
* create an onboarding feature, including the user creation then selection and the multiple cycles mechanics
* when pausing running session, the gif behind the dialog keeps moving... find a way to freeze this if possible
* we follow system dark/light theme switch, maybe we could add a choice in settings to let user decide? (follow system (would be default), force dark, force light)
* add home screen shortcut launchers for start session and statistics
* Nice to have and study case: [particles animation](https://proandroiddev.com/creating-a-particle-explosion-animation-in-jetpack-compose-4ee42022bbfa)
* [performance study case](https://proandroiddev.com/jetpack-compose-tutorial-improving-performance-in-dribbble-audio-app-b19848cf12e3)

