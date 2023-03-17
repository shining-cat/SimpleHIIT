
* inject dispatchers everywhere needed and handle thread choice from inside every suspend method. See https://developer.android.com/kotlin/coroutines/coroutines-best-practices#main-safe 
* loading screens are broken, progress indicator is stuck at the top of screen
* fix broken layout in landscape
* when making pictures for GIFS, insert watermark "@SimpleHIIT by Shining-cat" on body for each one
* See SimpleHiitDataStoreManagerImplTest -> how to trigger throwing exception from test dataStore?
* fix test coverage task for instrumented tests not reporting any coverage
* add dependencies versions update check plugin
* setup KTLINT
* CI github actions: run tests + linter before merge
* discuss filtering out CancellationException: how relevant is it to do it multiple times in a flow of data
* add plugin to check dependencies updates
* discuss: split clean arch layers to modules to allow for future multiple form factor builds
* if splitting into modules, add inter-modules dependencies graph generator plugin: 
  * classpath "com.vanniktech:gradle-dependency-graph-generator-plugin:0.8.0"
  * apply plugin: "com.vanniktech.dependency.graph.generator"
* check https://github.com/android/wear-os-samples/tree/main/WearVerifyRemoteApp
* TODO? if we want a complete evaluation of streaks, we need to store the locale at the time of recording alongside the timestamp for the session date. Then we can provide it to the usecase for evaluation.
* translate to FR and SV. Maybe add language selection in settings to be able to demo it?
* we handle system black/light theme switch, maybe we could add a choice in settings to let user decide? (follow system (would be default), force dark, force light)
* add home screen shortcut launchers for start session and statistics
* form factor UX differences: phone should maybe not offer multi-users?