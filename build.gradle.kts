// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()

    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.5.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        classpath ("org.jacoco:org.jacoco.core:0.8.5")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

enableFullReport()

tasks.register<Delete>("clean") {
    delete(buildDir)
}
