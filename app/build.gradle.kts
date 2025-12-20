plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.finalexam.musicboxx"
    compileSdk = 34 // Giữ nguyên 34 là ổn định nhất hiện nay

    defaultConfig {
        applicationId = "com.finalexam.musicboxx"
        minSdk = 24
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
}

dependencies {
    // --- NAVIGATION ---
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    // --- CORE ANDROID (ĐÃ HẠ CẤP VỀ BẢN ỔN ĐỊNH) ---
    // Thay vì dùng libs.androidx..., ta dùng chuỗi cứng để đảm bảo version đúng
    implementation("androidx.core:core-ktx:1.13.1") // Bản ổn định cho SDK 34
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity-ktx:1.9.0") // Bản ổn định, tránh lỗi API 36
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // --- FIREBASE ---
    implementation(platform("com.google.firebase:firebase-bom:33.1.0")) // Update BOM mới hơn chút
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")

    // --- COROUTINES & LIFECYCLE ---
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")

    // --- GLIDE (LOAD ẢNH) ---
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // --- MEDIA3 (PHÁT NHẠC) ---
    val media3Version = "1.3.1" // Bản ổn định mới
    implementation("androidx.media3:media3-exoplayer:$media3Version")
    implementation("androidx.media3:media3-ui:$media3Version")
    implementation("androidx.media3:media3-session:$media3Version")
    implementation("androidx.media3:media3-common:$media3Version")

    // --- TESTING ---
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}