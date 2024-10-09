/**
 * To define dependencies
 */
object Deps {
    val appCompat by lazy { "androidx.appcompat:appcompat:${Versions.APP_COMPAT}" }
    val datastore by lazy { "androidx.datastore:datastore-preferences:${Versions.DATASTORE}" }
    val materialDesign by lazy { "com.google.android.material:material:${Versions.MATERIAL}" }
    val androidXLifeCycleProcess by lazy { "androidx.lifecycle:lifecycle-process:${Versions.ANDROIDX_LIFECYCLE_PROCESS}" }
    val coil by lazy { "io.coil-kt:coil-compose:${Versions.COIL}" }
    val coilGif by lazy { "io.coil-kt:coil-gif:${Versions.COIL}" }
}
