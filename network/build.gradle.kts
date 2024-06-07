plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

val ACCESS_KEY: String by project
val SECRET_KEY: String by project
val REGION: String by project
val BUCKET_PATH: String by project
val DIR_NAME: String by project
val BASE_IMAGE_URL: String by project
val BASE_URL: String by project

android {
    namespace = "org.meetcute.network"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            buildConfigField("String", "ACCESS_KEY", ACCESS_KEY)
            buildConfigField("String", "SECRET_KEY", SECRET_KEY)
            buildConfigField("String", "REGION", REGION)
            buildConfigField("String", "BUCKET_PATH", BUCKET_PATH)
            buildConfigField("String", "DIR_NAME", DIR_NAME)
            buildConfigField("String", "BASE_IMAGE_URL", BASE_IMAGE_URL)
            buildConfigField("String", "BASE_URL", BASE_URL)
        }

        release {
            buildConfigField("String", "ACCESS_KEY", ACCESS_KEY)
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
    buildFeatures{
        buildConfig = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")



    //Retrofit and GSON
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    // room db
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // dagger hilt
    implementation("com.google.dagger:hilt-android-gradle-plugin:2.48")
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-compiler:2.51.1")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}