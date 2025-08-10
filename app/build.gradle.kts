plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}


android {
    namespace = "com.devianest.u3app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.devianest.u3app"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
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

    buildFeatures {
        compose = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Firebase
    implementation(libs.firebase.database)

    // RecyclerView (untuk komponen non-Compose)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.legacy.support.v4)

    // ✅ Compose BOM - ini akan mengatur semua versi Compose secara konsisten
    implementation(platform("androidx.compose:compose-bom:2024.02.02"))

    // ✅ Compose Core
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material:material") // Material 2
    implementation("androidx.compose.material3:material3") // Material 3 (opsional)

    // ✅ Compose Activity & Lifecycle
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // ✅ Navigation Compose - PENTING untuk Bottom Navigation
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // ✅ Icons Extended - untuk Material Icons
    implementation("androidx.compose.material:material-icons-extended")

    // ✅ Foundation & Animation
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.animation:animation")

    // ✅ ConstraintLayout untuk Compose (jika diperlukan)
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    // Image Loading
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation(libs.glide) // untuk View system

    // Data & Network
    implementation(libs.gson)
    implementation(libs.volley)
    implementation(libs.androidx.datastore.preferences)

    // ✅ Debug Tools
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

//Telegram Bot
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.datastore:datastore-preferences:1.0.0")

}