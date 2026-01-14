package fr.shiningcat.simplehiit.extensions

import com.android.build.api.dsl.CommonExtension
import fr.shiningcat.simplehiit.config.SimpleHiitBuildType
import org.gradle.api.Project

fun Project.configureBuildTypes(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.apply {
        buildTypes {
            getByName("release") {
                isMinifyEnabled = SimpleHiitBuildType.RELEASE.isMinifyEnabled
                enableUnitTestCoverage = SimpleHiitBuildType.RELEASE.enableUnitTestCoverage
                // ProGuard files only added by app convention plugins
            }
            getByName("debug") {
                // for some reason, applicationIdSuffix is not accessible here, we have to set it in the apps modules' build.gradle 'android' blocks
                isMinifyEnabled = SimpleHiitBuildType.DEBUG.isMinifyEnabled
                enableUnitTestCoverage = SimpleHiitBuildType.DEBUG.enableUnitTestCoverage
            }
        }
    }
}
