plugins {
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.kotlin.ksp)
    alias(libs.plugins.android.hilt)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.example.bachelorwork"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.bachelorwork"
        minSdk = 26
        targetSdk = 35
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

        create("profile") {
            initWith(getByName("debug"))
            isDebuggable = false
            isProfileable = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "11"
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
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    // Layout
    implementation(libs.flexbox.layout)

    //Paging
    implementation(libs.androidx.paging)

    //Analytics
    implementation(libs.vico.views)

    // Room
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    annotationProcessor(libs.androidx.room.compiler)

    // Hilt
    ksp(libs.compiler.hilt)
    implementation(libs.android.hilt)
    implementation(libs.hilt.navigation)

    // Serialization
    implementation(libs.serialization.json)
    
    //Simple Recycler View Adapter
    implementation(libs.element.adapter)

    //Firebase
    implementation(platform(libs.firebase.boom))
    implementation(libs.firebase.auth)

    //UCrop - Image Cropping
    implementation(libs.ucrop)

    //OkHttp
    implementation(libs.okhttp)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter)

    //Datastore Preferences
    implementation(libs.datastore.preferences)
    
    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(kotlin("reflect"))
}
