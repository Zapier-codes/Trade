plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.trade.core.navigation"
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
    // Slice 3 uses plain Material3 (Scaffold/TopAppBar/ListItem) for
    // EmptyRouteScreen/RouteDirectoryScreen. This is intentionally NOT the
    // real design system — core-ui's glass primitives (Slices 7-9) replace
    // these calls screen-by-screen as each route's real content lands.
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.8.0")
    // D1/Slice 9b: TradeNavHost now wires one real screen
    // (AppearanceSettingsScreen) instead of EmptyRouteScreen for the new
    // appearanceSettings route, per the D2+ convention already documented
    // in TradeRoute.kt ("point TradeNavHost at it... delete the
    // placeholder entry"). Needs core-ui (the screen itself) and
    // core-theme (WidgetStyle, passed through from the app-shell host) —
    // neither was a core-navigation dependency before this slice.
    implementation(project(":core-theme"))
    implementation(project(":core-ui"))
}
