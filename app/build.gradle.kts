plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-android-extensions")
}

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
