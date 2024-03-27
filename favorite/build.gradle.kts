plugins {
    id("com.android.dynamic-feature")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-parcelize")
    id("com.google.devtools.ksp")
}

apply {
    from ("../shared_dependencies.gradle")
}

android {
    namespace = "com.dicoding.favvie.favorite"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "KEY", "\"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjOTM1ODc2NzA0MTU1ZDU1MmUyNzhlMDg5NGQ3NWY3OSIsInN1YiI6IjY1ZmQ3ODg0MWIxZjNjMDE3Yzk3NmFmOCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.UG7o1ZxYcbfw1LTBMKtQ38JpzsCjAn9WszX2BxYgHKg\"")
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
        buildConfig = true
    }
}

dependencies {
    implementation (project(":core"))
    implementation (project(":app"))

    implementation("androidx.core:core-ktx:1.12.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.annotation:annotation:1.7.1")
}