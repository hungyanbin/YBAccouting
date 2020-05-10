plugins {
    id("com.android.library")
    id("kotlin-android")
    id("jacoco")
}

jacoco {
    toolVersion = Versions.jacoco
}
enableJacoco("**/view/**")

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.2")

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName  = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

}

dependencies {
    //androidx
    implementation(Dependencies.JetPack.appcompat)
    implementation(Dependencies.JetPack.coreKtx)

    //Date time
    implementation(Dependencies.klock)

    //test
    testImplementation(Dependencies.Test.junit)
    testImplementation(Dependencies.Test.assertj)
    testImplementation(Dependencies.Test.mockk)
}
