plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
//    id("com.google.dagger.hilt.android")
}


val ACCESS_KEY: String by project
val SECRET_KEY: String by project
val REGION: String by project
val BUCKET_PATH: String by project
val DIR_NAME: String by project
val BASE_IMAGE_URL: String by project
val BASE_URL: String by project
val AGORA_APP_ID: String by project

android {
    namespace = "org.meetcute"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.meetcute"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        renderscriptTargetApi = 31
        renderscriptSupportModeEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug{
            buildConfigField("String", "ACCESS_KEY", ACCESS_KEY)
            buildConfigField("String", "AGORA_APP_ID", AGORA_APP_ID)
            buildConfigField("String", "SECRET_KEY", SECRET_KEY)
            buildConfigField("String", "REGION", REGION)
            buildConfigField("String", "BUCKET_PATH", BUCKET_PATH)
            buildConfigField("String", "DIR_NAME", DIR_NAME)
            buildConfigField("String", "BASE_IMAGE_URL", BASE_IMAGE_URL)
            buildConfigField("String", "BASE_URL", BASE_URL)
        }
        release {
            buildConfigField("String", "ACCESS_KEY", ACCESS_KEY)
            buildConfigField("String", "AGORA_APP_ID", AGORA_APP_ID)
            buildConfigField("String", "SECRET_KEY", SECRET_KEY)
            buildConfigField("String", "REGION", REGION)
            buildConfigField("String", "BUCKET_PATH", BUCKET_PATH)
            buildConfigField("String", "DIR_NAME", DIR_NAME)
            buildConfigField("String", "BASE_IMAGE_URL", BASE_IMAGE_URL)
            buildConfigField("String", "BASE_URL", BASE_URL)
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
        buildConfig = true
        //noinspection DataBindingWithoutKapt
        dataBinding = true
    }
}

dependencies {

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation(project(":network"))
    implementation("androidx.activity:activity:1.9.0")
    val navVersion = "2.7.7"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    implementation("jp.wasabeef:blurry:4.0.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.hbb20:ccp:2.7.3")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    //Retrofit and GSON
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    implementation("io.agora.rtc:full-sdk:4.3.1")

    // AWS
    implementation("com.amazonaws:aws-android-sdk-s3:2.75.0")

    // loader
    implementation("com.github.razaghimahdi:Android-Loading-Dots:1.3.2")

//    implementation("androidx.camera:camera-camera2:1.3.3")

    // exo player
    implementation("com.google.android.exoplayer:exoplayer-core:2.19.1")
    implementation("com.google.android.exoplayer:exoplayer-ui:2.19.1")
//    implementation("com.google.android.exoplayer:exoplayer-cache:2.19.1")

    // dagger hilt
    implementation("com.google.dagger:hilt-android-gradle-plugin:2.48")

    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-compiler:2.51.1")


    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("com.github.vihtarb:tooltip:0.1.9")

    implementation ("io.socket:socket.io-client:2.1.0")
    implementation("io.agora.rtm:rtm-sdk:2.1.11")
    ///firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-analytics")



}