plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.storyapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.storyapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    /*implementation("androidx.core:core-ktx:1.9.0")*/
    implementation("androidx.core:core-ktx:1.13.1")

    /*ApiConfig*/
    implementation("com.squareup.retrofit2:retrofit:2.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.retrofit2:converter-gson:2.10.0")

    /*Userpreference datastore*/
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    /*Camera*/
    implementation("androidx.camera:camera-camera2:1.3.3")
    implementation("androidx.camera:camera-lifecycle:1.3.3")
    implementation("androidx.camera:camera-view:1.3.3")

    /*Util*/
    implementation("androidx.exifinterface:exifinterface:1.3.7")

    /*LoginView*/
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.0")

    /*ViewModel*/
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")

    /*Activity*/
    implementation("androidx.activity:activity-ktx:1.9.0")

    /*Coil buat load photo*/
    implementation("io.coil-kt:coil:2.6.0")

    /*Circle image*/
    implementation("de.hdodenhof:circleimageview:3.1.0")

    /*Jetbrains*/
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}