plugins {
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.kotlin.ksp)
    alias(libs.plugins.android.hilt)
}

android {
    namespace = "com.example.bachelorwork"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.bachelorwork"
        minSdk = 26
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
    buildFeatures {
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // AndroidX Libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.activity) 
    implementation(libs.androidx.fragment)

    // UI Components
    implementation(libs.androidx.swiperefreshlayout) // SwipeRefreshLayout
    implementation(libs.android.gms.codeScanner) // Barcode Scanner
    implementation(libs.glide) // Image loading

    // Navigation Components
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.navigation.fragment)

    // Layout
    implementation(libs.flexbox.layout)

    // Room
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room)
    implementation(libs.androidx.room.ktx)
    annotationProcessor(libs.androidx.room.compiler)

    // Hilt
    ksp(libs.compiler.hilt)
    implementation(libs.android.hilt)

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
