package com.trade.app.domain

/**
 * Placeholder domain model for D1/Slice 2 — exists only to demonstrate the
 * domain -> data -> presentation contract chain. Real domain models (User,
 * Trade, Portfolio, etc.) arrive with their own feature slices starting D2.
 */
data class AppBuildInfo(
    val versionName: String,
    val isDemoMode: Boolean,
)

/**
 * Domain-layer contract. The domain layer never knows about the fake
 * (D-phase) or real (R-phase) implementation — only this interface.
 * [com.trade.app.data.FakeAppBuildInfoRepository] is the D-phase
 * implementation; a real implementation replaces it in R-phase without
 * this interface or [GetAppBuildInfoUseCase] needing to change.
 */
interface AppBuildInfoRepository {
    suspend fun getBuildInfo(): AppBuildInfo
}

/**
 * Placeholder use case — the "placeholder use-cases" this slice's D-phase
 * focus calls for. Depends only on the domain-layer [AppBuildInfoRepository]
 * contract, never on a concrete implementation.
 */
class GetAppBuildInfoUseCase(
    private val repository: AppBuildInfoRepository,
) : NoParamsUseCase<AppBuildInfo> {
    override suspend fun invoke(): AppBuildInfo = repository.getBuildInfo()
}
