package com.trade.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trade.app.AppContainer
import com.trade.app.domain.GetAppBuildInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * D1/Slice 2 skeleton ViewModel — demonstrates the presentation -> domain
 * -> data contract chain every later feature ViewModel follows.
 *
 * `@JvmOverloads` generates a no-arg constructor so the default,
 * reflection-based `viewModels()` factory can instantiate this without a
 * DI framework — remove it once R1 wires real DI and this takes a
 * DI-injected constructor instead.
 */
class AppShellViewModel @JvmOverloads constructor(
    private val getAppBuildInfo: GetAppBuildInfoUseCase = AppContainer.getAppBuildInfoUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AppShellUiState>(AppShellUiState.Loading)
    val uiState: StateFlow<AppShellUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val info = getAppBuildInfo()
            _uiState.value = AppShellUiState.Loaded(
                versionName = info.versionName,
                isDemoMode = info.isDemoMode,
            )
        }
    }
}
