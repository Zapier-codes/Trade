package com.trade.app.data

import com.trade.app.domain.AppBuildInfo
import com.trade.app.domain.AppBuildInfoRepository
import kotlinx.coroutines.delay

/**
 * D-phase fake repository: canned response with a fake `delay()` to
 * simulate latency, per HANDOVER.md's D-phase rule ("in-memory fake
 * repositories that return canned responses, optionally with a fake
 * delay()"). No network call, no real data source.
 *
 * R-phase replaces this with a real implementation of
 * [AppBuildInfoRepository] — the domain layer and
 * [com.trade.app.domain.GetAppBuildInfoUseCase] don't change.
 */
class FakeAppBuildInfoRepository : AppBuildInfoRepository {
    override suspend fun getBuildInfo(): AppBuildInfo {
        delay(150)
        return AppBuildInfo(
            versionName = "TRADE-0.1.0-dummy",
            isDemoMode = true,
        )
    }
}
