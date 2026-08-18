package com.trade.app.presentation

/**
 * Placeholder UI state — the sealed-state convention every later
 * feature ViewModel should follow: a `Loading` state and a `Loaded` state
 * carrying exactly what the screen needs, nothing else.
 */
sealed interface AppShellUiState {
    data object Loading : AppShellUiState
    data class Loaded(val versionName: String, val isDemoMode: Boolean) : AppShellUiState
}
