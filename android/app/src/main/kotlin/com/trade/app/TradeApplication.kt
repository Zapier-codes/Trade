package com.trade.app

import android.app.Application

/**
 * TRADE application entry point.
 *
 * D1/Slice 1b: bare shell only. DI wiring (Hilt/Koin — decision still open,
 * see docs/HANDOVER.md Section 6) lands in Slice 2 (Clean Architecture
 * skeleton). Do not add module init logic here until that decision is made.
 */
class TradeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Intentionally empty for D1/Slice 1b.
    }
}
