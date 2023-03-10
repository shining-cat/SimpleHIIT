
* rename composable so: main part is a "...screen". 
  * It has an overload with the same name to allow for Preview to not have to provide everything as runtime
  * this in turn contains a Scaffold that displays a "...topbar" and a "...content". Both live in the same file as the screen
  * The content handles the screen states switch and calls the adequate "[state name]Content". These should be extracted as independent files
  * below this composables should be called "...component", except if they have been moved to the _components_ package to be reused in more screens.
  * The content also handles the dialog states and calls directly a component, as this should be rather small. It can be either a component from the components package or a custom one, in which case it should be extracted to its own file
* fix broken layout in landscape
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