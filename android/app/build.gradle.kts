plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.trade.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.trade.app"
        minSdk = 26
        targetSdk = 35
        // CI stamps these per-build via -PappVersionCode / -PappVersionName
        // (see .github/workflows/ci.yml). Local/manual builds fall back to
        // the dummy defaults below.
        versionCode = (project.findProperty("appVersionCode") as String?)?.toIntOrNull() ?: 1
        versionName = (project.findProperty("appVersionName") as String?) ?: "TRADE-0.1.0-dummy"
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

    // Demo/Live build variants land for real in Slice 15 (Demo/Live toggle
    // shell) and get finalized in R1 — flavor dimensions intentionally
    // not declared yet so we don't guess ahead of that slice.
}

dependencies {
    implementation(project(":core-theme"))
    implementation(project(":core-ui"))
    implementation(project(":core-navigation"))

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation(platform("androidx.compose:compose-bom:2024.09.02"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
}
