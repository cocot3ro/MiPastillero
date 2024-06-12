plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.safeArgs)
    alias(libs.plugins.ksp)
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

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.flexbox)
    implementation(libs.androidx.fragment.ktx)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.preference)

    //Corrutinas
    implementation(libs.kotlinx.coroutines.android)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //Dagger Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    //Room
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Security
    implementation(libs.androidx.security.crypto)

    // Sqlcipher
    implementation(libs.android.database.sqlcipher)

    // Glide
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter.api)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}