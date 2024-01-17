plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")

}

secrets {
    // Optionally specify a different file name containing your secrets.
    // The plugin defaults to "local.properties"
    propertiesFileName = "secrets.properties"

    // A properties file containing default secret values. This file can be
    // checked in version control.
    defaultPropertiesFileName = "local.properties"

    // Configure which keys should be ignored by the plugin by providing regular expressions.
    // "sdk.dir" is ignored by default.
    ignoreList.add("keyToIgnore") // Ignore the key "keyToIgnore"
    ignoreList.add("sdk.*")       // Ignore all keys matching the regexp "sdk.*"
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
    implementation("com.google.android.gms:play-services-location:21.0.1")
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

    //Circular ImageView
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    // Recycler View
    implementation ("androidx.recyclerview:recyclerview:1.3.2")

    // Retrofit + GSON + OkHttpClientBOM + Interceptor
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    // MultiDex
    implementation ("androidx.multidex:multidex:2.0.1")

    // Gson
    implementation ("com.google.code.gson:gson:2.10.1")

    // Motion Toast
    implementation ("com.github.Spikeysanju:MotionToast:1.4")

    // Lottie Animations
    implementation ("com.airbnb.android:lottie:6.2.0")

    // Google Maps
    implementation ("com.google.android.gms:play-services-maps:18.2.0")

    // Preference
    implementation ("androidx.preference:preference-ktx:1.2.1")

    // SwipeRefresh Layout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    //Shimmer Effect
    implementation ("com.facebook.shimmer:shimmer:0.1.0@aar")

    // Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    // Location Service
    implementation ("com.google.android.gms:play-services-location:21.0.1")

    // Ticker Animation
    implementation ("com.robinhood.ticker:ticker:2.0.4")



}
kapt {
    correctErrorTypes = true
}
