plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.umc.umc_6th_wit_android"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.umc.umc_6th_wit_android"
        minSdk = 23
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

    viewBinding {
        enable = true
    }

    dataBinding {
        enable = true
    }
}



dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    implementation("me.relex:circleindicator:2.1.6")  //circleindicator
    implementation("androidx.viewpager2:viewpager2:1.0.0")

    implementation ("com.tbuonomo:dotsindicator:5.0")

    implementation ("androidx.fragment:fragment-ktx:1.6.1")


    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.retrofit2:adapter-rxjava2:2.9.0")

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")


    //FlexboxLayout
    implementation ("com.google.android.flexbox:flexbox:3.0.0")


    implementation("com.google.code.gson:gson:2.8.7")    //GSON

    implementation ("com.google.android.material:material:1.10.0")

    implementation("androidx.core:core-splashscreen:1.0.1")

    implementation("com.navercorp.nid:oauth-jdk8:5.9.1") // jdk 8


    //String 형태의 URL은 사용할 수 없음.Glide 필요
    implementation ("com.github.bumptech.glide:glide:4.15.1")
}