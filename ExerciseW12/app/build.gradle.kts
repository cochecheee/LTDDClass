plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.bt1"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.bt1"
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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Alternative Supabase Java implementation
    implementation("org.postgresql:postgresql:42.5.1")
    implementation("io.github.cdimascio:java-dotenv:5.2.2")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("org.json:json:20230227")
    implementation("io.github.jan-tennert.supabase:gotrue-kt:1.1.0")

    // For async operations
    implementation("androidx.concurrent:concurrent-futures:1.1.0")
}