plugins {
    alias(libs.plugins.android.application)
}

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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.viewpager) // ViewPager (if not already included)
    implementation(libs.circleindicator)     // CircleIndicator library
    // Add other dependencies like Glide if needed
    implementation(libs.glide)
    annotationProcessor(libs.compiler)
    implementation("me.relex:circleindicator:2.1.6'")
    // SliderView library
//    implementation("com.github.smarteist:autoimageslider:1.4.0-appcompat")
    implementation("com.github.koai-dev:Android-Image-Slider:1.4.0")

    // Glide (or Picasso) is needed for image loading with SliderView
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
}