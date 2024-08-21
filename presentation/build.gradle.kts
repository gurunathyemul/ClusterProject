plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("kapt")
//    alias(libs.plugins.kspLibrary)//ksp-Kotlin symbol processing(Migrate from kapt to KSP)
    alias(libs.plugins.hiltLibrary)//hilt
}

android {
    namespace = "com.example.clusterproject"
    compileSdk = 34

    buildFeatures {
        dataBinding = true
        aidl=true
    }

    defaultConfig {
        applicationId = "com.example.clusterproject"
        minSdk = 26
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
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //Add module
    implementation(project(path = ":data"))
    implementation(project(path = ":domain"))

    //lifecycle
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)

    //hilt dependencies
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    //coroutine
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    //view model scope
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.javax.inject)

    //kotlin coil to load image
    implementation(libs.coil)

    //maps
    //sdk
    implementation("com.mappls.sdk:place-widget:2.1.0")
    implementation ("com.mappls.sdk:mappls-android-sdk:8.0.8")
  //Annotation Pl("ugin")
    implementation("com.mappls.sdk:annotation-plugin:1.0.1")

    /*
        implementation("com.mappls.sdk:directions-plugin:1.0.1")

        implementation("com.mappls.sdk:scalebar-plugin:1.0.0")
        implementation("com.mappls.sdk:geofence-ui:1.0.0")

        //for nearby wi("dget")
        implementation("com.mappls.sdk:nearby-ui:1.0.2")

        implementation("com.mappls.sdk:driving-range-plugin:1.0.0")
        //for direction(" widget")
        implementation("com.mappls.sdk:direction-ui:1.2.2")
        //Geoanalytics ("plugin")
        implementation("com.mappls.sdk:geoanalytics-plugin:1.0.0")
        //for location
        //implementatio("n 'com.google.android.gms:play-services-location:21.0.1'")

        implementation("com.github.QuadFlask:colorpicker:0.0.15")*/
//    implementation ("com.mappls.sdk:mappls-navigation-sdk:1.0.0")

}
kapt {
    correctErrorTypes = true
}

