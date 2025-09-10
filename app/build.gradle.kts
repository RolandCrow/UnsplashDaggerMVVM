plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.unsplashdaggermvvm"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.unsplashdaggermvvm"
        minSdk = 26
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //navigation
    implementation (libs.androidx.navigation.fragment.ktx.v234)
    implementation (libs.androidx.navigation.ui.ktx.v234)

    //recyclerview
    implementation (libs.androidx.recyclerview)
    implementation (libs.androidx.fragment.ktx)
    implementation (libs.androidx.legacy.support.v4)
    implementation (libs.dagger)
    implementation (libs.dagger.android.support)
    ksp (libs.dagger.compiler)
    ksp (libs.dagger.android.processor)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.logging.interceptor)
    implementation (libs.androidx.paging.runtime.ktx)
    implementation (libs.glide)
    annotationProcessor (libs.compiler)
    implementation (libs.androidx.swiperefreshlayout)
    implementation (libs.androidx.datastore.preferences)
    implementation (libs.kotlin.reflect)
    implementation (libs.androidx.palette.ktx)
}