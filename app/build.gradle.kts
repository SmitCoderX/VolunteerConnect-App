plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.smitcoderx.volunteerconnect"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.smitcoderx.volunteerconnect"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")

    // SDP + SSP
    implementation ("com.intuit.sdp:sdp-android:1.1.0")
    implementation ("com.intuit.ssp:ssp-android:1.1.0")

    // Dagger Hilt
    implementation ("com.google.dagger:hilt-android:2.46")
    kapt ("com.google.dagger:hilt-compiler:2.46")

    // Loading Button
    implementation ("com.github.leandroborgesferreira:loading-button-android:2.3.0")

    //Circular ImageView
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    // Recycler View
    implementation ("androidx.recyclerview:recyclerview:1.3.2")

    // Retrofit + Moshi
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // MultiDex
    implementation ("androidx.multidex:multidex:2.0.1")

    // Gson
    implementation ("com.google.code.gson:gson:2.10.1")

    // Motion Toast
    implementation ("com.github.Spikeysanju:MotionToast:1.4")

    // Lottie Animations
    implementation ("com.airbnb.android:lottie:6.2.0")

}
kapt {
    correctErrorTypes = true
}
