plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("kapt")
    //kapt-kotlin annotation processing tool
    alias(libs.plugins.kspLibrary)//ksp-Kotlin symbol processing(Migrate from kapt to KSP)
    alias(libs.plugins.hiltLibrary)//hilt
}

android {
    namespace = "com.example.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = "18"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //domain module
    implementation(project(":domain"))

    //hilt dependencies
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    //data store
    implementation(libs.androidx.datastore)
    //room
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    //gson
    implementation(libs.gson)
    //retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    //inject
    implementation(libs.javax.inject)

}
// Allow references to generated code
kapt {
    correctErrorTypes = true
}