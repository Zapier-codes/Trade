plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.trade.core.theme"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.09.02"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    // D1/Slice 10 — ThemeEventBus (SharedFlow<ThemeEvent>) needs
    // kotlinx-coroutines-core for MutableSharedFlow/SharedFlow. Only the
    // platform-agnostic `-core` artifact, not `-android` (no Dispatchers.Main
    // usage here) — `app` already depends on `-android` for its own
    // ViewModel/coroutine scope needs, unrelated to this.
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
}
