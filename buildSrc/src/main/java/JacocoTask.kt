import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

fun Project.enableJacoco(vararg fileFilter: String) {

    tasks.withType<Test> {
        configure<JacocoTaskExtension> {
            isIncludeNoLocationClasses = true
        }
    }

    tasks.register<JacocoReport>("jacocoUnitTestReport") {
        dependsOn("testDebugUnitTest")

        val buildDir = "build"
        val coverageSourceDir = "src/main/java"

        val kotlinClasses = fileTree("$buildDir/tmp/kotlin-classes/debug")
            .include(*fileFilter)

        classDirectories.setFrom(files(kotlinClasses))
        additionalSourceDirs.setFrom(files(coverageSourceDir))
        sourceDirectories.setFrom(file(coverageSourceDir))
        executionData.setFrom(fileTree(buildDir).include("jacoco/testDebugUnitTest.exec"))

        reports {
            xml.isEnabled = true
            html.isEnabled = true
        }
    }
}