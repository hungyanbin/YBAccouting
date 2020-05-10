object Dependencies {
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    object JetPack {
        const val appcompat = "androidx.appcompat:appcompat:${Versions.andridx}"
        const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.andridx}"
        const val material = "com.google.android.material:material:${Versions.andridx}"
        const val coreKtx = "androidx.core:core-ktx:${Versions.androidxKtx}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
        const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
        const val room = "androidx.room:room-runtime:${Versions.room}"
        const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
        const val roomAnnotationProcessor = "androidx.room:room-compiler:${Versions.room}"
        const val roomKapt = "androidx.room:room-compiler:${Versions.room}"
    }

    object Koin {
        const val koinScope = "org.koin:koin-androidx-scope:${Versions.koin}"
        const val koinViewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    }

    const val klock = "com.soywiz.korlibs.klock:klock:1.7.3"

    object Test {
        const val junit = "junit:junit:4.12"
        const val assertj = "org.assertj:assertj-core:3.11.1"
        const val mockk = "io.mockk:mockk:1.10.0"
        const val androidRunner = "androidx.test:runner:1.2.0"
        const val androidJunit = "androidx.test.ext:junit:1.1.1"
    }
}