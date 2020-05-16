import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.tasks.JacocoReport

fun Project.enableFullReport() {
    allprojects {
        enableJacoco(
            //Auto generate files
            "**/R.class",
            "**/R\$*.class",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "android/**/*.*",
            //View layer files
            "**/*Activity*",
            "**/*Application*",
            "**/*Dialog*",
            "**/*Modules*"
        )
    }

    plugins.apply("jacoco")

    tasks.register<JacocoReport>("jacocoFullReport") {

        println("start jacocoFullReport...")

        val subprojectTasks = subprojects.map { it.getTasksByName("jacocoUnitTestReport", false) }
            .flatten()
            .filterIsInstance<JacocoReport>()

        dependsOn(*subprojectTasks.toTypedArray())
        
        val source = files(*subprojectTasks.mapToArray { it.sourceDirectories })
        val classDirs = files(*subprojectTasks.mapToArray { it.classDirectories })
        val executeData = files(*subprojectTasks.mapToArray { it.executionData })

        additionalSourceDirs.setFrom(source)
        sourceDirectories.setFrom(source)
        classDirectories.setFrom(classDirs)
        executionData.setFrom(executeData)

        reports {
            with(html) {
                isEnabled = true
                destination = file("build/reports/jacoco")
            }
        }

    }

}

fun Project.enableJacoco(vararg fileFilter: String) {
    plugins.apply("jacoco")

    tasks.register<JacocoReport>("jacocoUnitTestReport") {
        dependsOn("testDebugUnitTest")

        val buildDir = "build"
        val coverageSourceDir = "src/main/java"

        val kotlinClasses = fileTree("$buildDir/tmp/kotlin-classes/debug")
            .exclude(*fileFilter)

        classDirectories.setFrom(files(kotlinClasses))
        additionalSourceDirs.setFrom(files(coverageSourceDir))
        sourceDirectories.setFrom(file(coverageSourceDir))
        executionData.setFrom(fileTree(buildDir).include("jacoco/testDebugUnitTest.exec"))

        reports {
            csv.isEnabled = false
            xml.isEnabled = false
            html.isEnabled = true
        }
    }
}

private inline fun <T, reified R> List<T>.mapToArray(transform: (T) -> R): Array<R> {
    return this.map { transform.invoke(it) }
        .toTypedArray()
}
