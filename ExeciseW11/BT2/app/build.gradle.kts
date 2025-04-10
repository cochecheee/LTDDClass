plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
//    alias(libs.plugins.google.services) // if your alias is google-services
}

//buildscript {
//    dependencies {
//        classpath("com.google.gms:google-services:4.3.15")
//    }
//}

android {
    namespace = "com.example.bt2"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.bt2"
        minSdk = 24
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

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.google.android.gms:play-services-auth:20.4.1") // Có thể không cần nếu chỉ dùng DB/Storage

    // --- Supabase Kotlin Client (Nên dùng ngay cả với Java) ---
    implementation("io.github.jan-tennert.supabase:postgrest-kt:1.0.0") // Cho Database
    implementation("io.github.jan-tennert.supabase:storage-kt:1.0.0")  // Cho Storage

    // --- Thư viện hỗ trợ Supabase Client ---
    // Coroutines (Bắt buộc cho supabase-kt)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")// Hoặc phiên bản mới hơn
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // Hoặc phiên bản mới hơn
    // Lifecycle Scope (Để chạy Coroutines an toàn trong Activity/Fragment)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0") // Hoặc phiên bản mới hơn
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")

    implementation("com.google.code.gson:gson:2.10.1")
}