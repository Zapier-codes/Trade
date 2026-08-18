package com.trade.app

import com.trade.app.data.FakeAppBuildInfoRepository
import com.trade.app.domain.AppBuildInfoRepository
import com.trade.app.domain.GetAppBuildInfoUseCase

/**
 * Manual composition root — temporary. DI framework choice (Hilt vs Koin)
 * is explicit R1 phase-acceptance scope (see docs/HANDOVER.md "Phase
 * acceptance (R1)"), not D1, so this file exists purely to keep Slice 2's
 * skeleton wireable without prejudging that decision.
 *
 * Do not scale this pattern into feature modules (D2+) — each new
 * feature's fake wiring belongs in its own module, not funneled through
 * this object. When R1 lands, this file is deleted and replaced by real
 * DI modules/graphs.
 */
object AppContainer {
    private val appBuildInfoRepository: AppBuildInfoRepository = FakeAppBuildInfoRepository()
    val getAppBuildInfoUseCase = GetAppBuildInfoUseCase(appBuildInfoRepository)
}
