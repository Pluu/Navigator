package dependencies

object Dep {
    object GradlePlugin {
        const val agpVersion = "4.1.3"
        const val android = "com.android.tools.build:gradle:$agpVersion"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
    }
    object AndroidX {
        const val activityKtx = "androidx.activity:activity-ktx:1.2.0"
        const val appCompat = "androidx.appcompat:appcompat:1.2.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
        const val coreKtx = "androidx.core:core-ktx:1.3.2"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:1.3.0"
        const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:2.3.0"
        const val material = "com.google.android.material:material:1.3.0"
        const val viewBinding = "androidx.databinding:viewbinding:${GradlePlugin.agpVersion}"

        object Test {
            const val core = "androidx.test:core:1.3.0"
            const val junit = "androidx.test.ext:junit:1.1.2"
            const val espressoCore = "androidx.test.espresso:espresso-core:3.3.0"
        }
    }

    object Kotlin {
        const val version = "1.4.31"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${version}"
    }

    object Test {
        private const val mockitoVersion = "3.8.0"
        const val mockito = "org.mockito:mockito-core:${mockitoVersion}"
        const val mockitoInline = "org.mockito:mockito-inline:${mockitoVersion}"
        const val junit = "junit:junit:4.13.1"
        const val robolectric = "org.robolectric:robolectric:4.5.1"
    }

    const val gson = "com.google.code.gson:gson:2.8.6"
    const val timber = "com.jakewharton.timber:timber:4.7.1"

}