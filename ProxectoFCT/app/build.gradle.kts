plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.safeArgs)
    id("kotlin-kapt")
}

android {
    namespace = "com.a23pablooc.proxectofct"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.a23pablooc.proxectofct"
        minSdk = 30
        targetSdk = 34
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
    buildFeatures {
        viewBinding = true
    }
}

kapt {
    correctErrorTypes = true
}

//noinspection KaptUsageInsteadOfKsp
dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.fragment.ktx)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    //Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.activity.ktx)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //Dagger Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.preference)
    kapt(libs.hilt.compiler)

    //Room
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

//    TODO: Borrar esta dependencia al implementar Retrofit
    implementation(libs.khttp)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}