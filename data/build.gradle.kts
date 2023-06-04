plugins{
    id("libraries_gradle_config")
    id("com.google.dagger.hilt.android")//TODO: find out if we can include this in libraries_gradle_config: it causes a resolution error...
}

android {
    namespace = "fr.shining_cat.simplehiit.data"
}

dependencies {
    implementation(project(":commonDomain"))
    implementation(project(":commonUtils"))
    testImplementation(project(":testUtils"))
    androidTestImplementation(project(":testUtils"))
    //
    implementation(HiltDeps.hiltAndroid)
    kapt(HiltDeps.hiltAndroidCompiler)
    implementation(Deps.datastore)
    implementation(RoomDeps.roomRuntime)
    implementation(RoomDeps.roomCoroutinesExtensions)
    kapt(RoomDeps.roomKaptCompiler)
    //
    testImplementation(HiltDeps.hiltTestAndroid)
    testImplementation(TestDeps.coroutinesTest)
    testImplementation(TestDeps.mockk)
    testImplementation(TestDeps.jupiter)
}