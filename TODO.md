
* See SimpleHiitDataStoreManagerImplTest -> how to trigger throwing exception from test dataStore?
* fix test coverage task for instrumented tests not reporting any coverage
* add dependencies versions update check plugin
* setup KTLINT
* CI github actions: run tests + linter before merge
* discuss filtering out CancellationException: how relevant is it to do it multiple times in a flow of data
* add plugin to check dependencies updates
* discuss: split clean arch layers to module to allow for future multiple form factor builds
* if splitting into modules, add inter-modules dependencies graph generator plugin: 
  * classpath "com.vanniktech:gradle-dependency-graph-generator-plugin:0.8.0"
  * apply plugin: "com.vanniktech.dependency.graph.generator"
* check https://github.com/android/wear-os-samples/tree/main/WearVerifyRemoteApp
* TODO? if we want a complete evaluation of streaks, we need to store the locale at the time of recording alongside the timestamp for the session date. Then we can provide it to the usecase for evaluation.