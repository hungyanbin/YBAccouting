plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-android-extensions")
    id("jacoco")
}

//Jacoco block
jacoco {
    toolVersion = Versions.jacoco
}

tasks.withType<Test> {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
    }
}

tasks.register<JacocoReport>("jacocoUnitTestReport") {
    dependsOn("testDebugUnitTest")

    val buildDir = "build"
    val coverageSourceDir = "src/main/java"
    val fileFilter = arrayOf(
        "**/domain/**",
        "**/data/**")

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

//end Jacoco block

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.2")

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName  = "1.0"
        applicationId = "com.yanbin.ybaccouting"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        getByName("debug") {
            isTestCoverageEnabled = true
        }
    }

    testOptions {
        execution = "ANDROID_TEST_ORCHESTRATOR"
        animationsDisabled = true
        unitTests.isIncludeAndroidResources = true
    }

}

dependencies {
    implementation(project(":calendarview"))

    implementation(Dependencies.kotlinStdLib)
    implementation(Dependencies.coroutines)

    //androidx
    implementation(Dependencies.JetPack.appcompat)
    implementation(Dependencies.JetPack.coreKtx)
    implementation(Dependencies.JetPack.constraintLayout)
    implementation(Dependencies.JetPack.recyclerView)
    implementation(Dependencies.JetPack.material)
    implementation(Dependencies.JetPack.viewModel)
    implementation(Dependencies.JetPack.liveData)
    //room
    implementation(Dependencies.JetPack.room)
    implementation(Dependencies.JetPack.roomKtx)
    annotationProcessor(Dependencies.JetPack.roomAnnotationProcessor)
    kapt(Dependencies.JetPack.roomKapt)

    //koin
    implementation(Dependencies.Koin.koinScope)
    implementation(Dependencies.Koin.koinViewModel)

    //Date time
    implementation(Dependencies.klock)

    //test
    testImplementation(Dependencies.Test.junit)
    testImplementation(Dependencies.Test.assertj)
    androidTestImplementation(Dependencies.Test.androidRunner)
    androidTestImplementation(Dependencies.Test.androidJunit)
}
