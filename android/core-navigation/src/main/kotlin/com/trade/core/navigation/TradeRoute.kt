package com.trade.core.navigation

/**
 * D1/Slice 3 — Navigation graph.
 *
 * Every route the product will ever need (per `docs/TRADE_BLUEPRINT_v2.md`
 * Section 6 "User Journeys" and `docs/HANDOVER.md` Section 4's 20-slice
 * tables for Topics 2-9) is registered here, up front, as an empty
 * destination. This lets [TradeNavHost] wire the *complete* graph now
 * (Slice 3's acceptance bar: "navigates between empty placeholder screens
 * for every planned route") while every screen's real content lands
 * feature-module-by-feature-module in D2-D9.
 *
 * **Convention for D2+ sessions:** when a feature module gets its own real
 * Composable for one of these routes, that module's `NavGraphBuilder`
 * extension replaces the corresponding entry in [TradeNavHost] — do not
 * duplicate the route id/path here, just point [TradeNavHost] at the real
 * screen and delete this route's entry from [placeholderRoutes] (leave the
 * [TradeRoute] constant itself, other routes' `path`s may reference it as
 * a nav target).
 *
 * Not every screen mentioned in a slice table gets its own route: fake
 * *sub-states* of one screen (e.g. a deposit success vs. fail banner) stay
 * inside that screen's own state, they are not separate destinations. Only
 * genuinely distinct navigable destinations are listed here.
 */
data class TradeRoute(
    val path: String,
    val title: String,
    /** Topic group this route belongs to, matches HANDOVER.md Section 4 topic titles. */
    val group: String,
    /** Where the route's real content lands, e.g. "D2.S01" / "D2.S01 & R2.S01". */
    val slice: String,
)

object TradeRoutes {

    const val ROUTE_DIRECTORY = "directory"

    // ---- Topic 2: Onboarding & Authentication ----
    val welcome = TradeRoute("onboarding/welcome", "Welcome", "Onboarding & Auth", "D2.S01")
    val tutorial = TradeRoute("onboarding/tutorial", "Tutorial (3 slides)", "Onboarding & Auth", "D2.S02-05")
    val termsOfService = TradeRoute("onboarding/tos", "Terms of Service", "Onboarding & Auth", "D2.S06")
    val onboardingHandoff = TradeRoute("onboarding/handoff", "Account Created / Handoff", "Onboarding & Auth", "D2.S07")
    val biometricPrompt = TradeRoute("auth/biometric", "Biometric Prompt", "Onboarding & Auth", "D2.S08")
    val pinFallback = TradeRoute("auth/pin", "PIN Fallback", "Onboarding & Auth", "D2.S09")
    val deviceBinding = TradeRoute("auth/device-binding", "Device Binding", "Onboarding & Auth", "D2.S10")
    val passkeySetup = TradeRoute("auth/passkey-setup", "Passkey Setup", "Onboarding & Auth", "D2.S11")
    val returningUserAuth = TradeRoute("auth/returning-user", "Returning User Auth", "Onboarding & Auth", "D2.S12")
    val seedPhraseDisplay = TradeRoute("auth/seed-phrase", "Seed Phrase Display", "Onboarding & Auth", "D2.S13 / D9.S09")
    val socialRecoverySetup = TradeRoute("auth/social-recovery-setup", "Social Recovery Setup", "Onboarding & Auth", "D2.S14 / D9.S10")
    val sessionDeviceManagement = TradeRoute("auth/session-devices", "Session / Device Management", "Onboarding & Auth", "D2.S15 / D9.S13")

    // ---- Topic 3: Dashboard & Portfolio ----
    val dashboard = TradeRoute("dashboard", "Dashboard", "Dashboard & Portfolio", "D3.S01")
    val assetDetail = TradeRoute("dashboard/asset-detail", "Asset Detail (market feed tap)", "Dashboard & Portfolio", "D3.S10")

    // ---- Topic 4: Trading Interface ----
    val pairSelector = TradeRoute("trading/pair-selector", "Pair Selector", "Trading Interface", "D4.S01")
    val tradingScreen = TradeRoute("trading/screen", "Trading Screen (chart + order entry)", "Trading Interface", "D4.S02-13")
    val orderConfirmation = TradeRoute("trading/order-confirmation", "Order Confirmation", "Trading Interface", "D4.S13")
    val openOrders = TradeRoute("trading/open-orders", "Open Orders", "Trading Interface", "D4.S14")
    val tradeHistory = TradeRoute("trading/history", "Trade History", "Trading Interface", "D4.S15")
    val positionManagement = TradeRoute("trading/positions", "Position Management", "Trading Interface", "D4.S16")
    val structuredProducts = TradeRoute("trading/structured-products", "Options / RWA / Structured Products", "Trading Interface", "D4.S19")

    // ---- Topic 5: Wallet, Deposits & Withdrawals ----
    val walletHome = TradeRoute("wallet/home", "Wallet Home", "Wallet & Payments", "D5.S01")
    val depositMethodPicker = TradeRoute("wallet/deposit/method", "Deposit — Method Picker", "Wallet & Payments", "D5.S02")
    val depositAmountEntry = TradeRoute("wallet/deposit/amount", "Deposit — Amount Entry", "Wallet & Payments", "D5.S03")
    val depositKorapayFlow = TradeRoute("wallet/deposit/korapay", "Deposit — Korapay (card/bank/mobile)", "Wallet & Payments", "D5.S04-06")
    val depositJuciwayFlow = TradeRoute("wallet/deposit/juciway", "Deposit — Juciway (crypto/Interac)", "Wallet & Payments", "D5.S07-09")
    val depositResult = TradeRoute("wallet/deposit/result", "Deposit Success / Fail", "Wallet & Payments", "D5.S10")
    val withdrawEntry = TradeRoute("wallet/withdraw/entry", "Withdraw — Asset/Amount/Address", "Wallet & Payments", "D5.S11")
    val withdrawBiometricConfirm = TradeRoute("wallet/withdraw/confirm", "Withdraw — Biometric Confirm", "Wallet & Payments", "D5.S12")
    val withdrawBroadcastStatus = TradeRoute("wallet/withdraw/status", "Withdraw — Broadcast Status", "Wallet & Payments", "D5.S13")
    val transactionHistory = TradeRoute("wallet/transactions", "Transaction History", "Wallet & Payments", "D5.S15 / Blueprint 10.6")

    // ---- Topic 6: AI Multi-Agent System ----
    val aiAgentEntry = TradeRoute("ai/entry", "AI Agent Entry / Strategy Picker", "AI Multi-Agent System", "D6.S01")
    val riskToleranceSelector = TradeRoute("ai/risk-tolerance", "Risk Tolerance Selector", "AI Multi-Agent System", "D6.S02")
    val aiActivationConfirmation = TradeRoute("ai/activation", "Activation Flow + Confirmation", "AI Multi-Agent System", "D6.S03")
    val agentStatusDashboard = TradeRoute("ai/agent-status", "Agent Status Dashboard", "AI Multi-Agent System", "D6.S04-08")
    val signalFeed = TradeRoute("ai/signal-feed", "Live Signal Feed", "AI Multi-Agent System", "D6.S09")
    val signalDetail = TradeRoute("ai/signal-detail", "Signal Detail (confidence, reasoning)", "AI Multi-Agent System", "D6.S10")
    val backtestConfig = TradeRoute("ai/backtest/config", "Backtesting — Config", "AI Multi-Agent System", "D6.S11")
    val backtestResults = TradeRoute("ai/backtest/results", "Backtesting — Results", "AI Multi-Agent System", "D6.S12")

    // ---- Topic 7: Copy Trading, P2P & Earn ----
    val copyLeaderboard = TradeRoute("copy/leaderboard", "Copy Trading — Leaderboard", "Copy Trading, P2P & Earn", "D7.S01")
    val traderProfile = TradeRoute("copy/trader-profile", "Trader Profile", "Copy Trading, P2P & Earn", "D7.S02")
    val copyAllocateFlow = TradeRoute("copy/allocate", "Copy Trading — Follow/Allocate", "Copy Trading, P2P & Earn", "D7.S03")
    val activeCopiesDashboard = TradeRoute("copy/active", "Active Copies Dashboard", "Copy Trading, P2P & Earn", "D7.S04")
    val socialCopyFeed = TradeRoute("copy/social-feed", "Social Copy Trading Feed", "Copy Trading, P2P & Earn", "D7.S05")
    val p2pListingBrowse = TradeRoute("p2p/browse", "P2P — Listing Browse", "Copy Trading, P2P & Earn", "D7.S06")
    val p2pCreateListing = TradeRoute("p2p/create", "P2P — Create Listing", "Copy Trading, P2P & Earn", "D7.S07")
    val p2pEscrowBuyer = TradeRoute("p2p/escrow/buyer", "P2P — Escrow (Buyer)", "Copy Trading, P2P & Earn", "D7.S08")
    val p2pEscrowSeller = TradeRoute("p2p/escrow/seller", "P2P — Escrow (Seller)", "Copy Trading, P2P & Earn", "D7.S09")
    val p2pDisputeChat = TradeRoute("p2p/dispute-chat", "P2P — Dispute / Chat", "Copy Trading, P2P & Earn", "D7.S10")
    val earnProductList = TradeRoute("earn/products", "Earn — Product List (savings/staking)", "Copy Trading, P2P & Earn", "D7.S11-13")
    val earnDepositFlow = TradeRoute("earn/deposit", "Earn — Deposit Into Product", "Copy Trading, P2P & Earn", "D7.S14")
    val earnWithdrawFlow = TradeRoute("earn/withdraw", "Earn — Withdraw / Unstake", "Copy Trading, P2P & Earn", "D7.S15")
    val earnActivePositions = TradeRoute("earn/positions", "Earn — Active Positions", "Copy Trading, P2P & Earn", "D7.S16")

    // ---- Topic 8: Notifications ----
    val notificationFeed = TradeRoute("notifications/feed", "Notification Feed", "Notifications", "D8.S01")
    val notificationDetail = TradeRoute("notifications/detail", "Notification Detail", "Notifications", "D8.S02")
    val notificationSettings = TradeRoute("notifications/settings", "Notification Settings", "Notifications", "D8.S03")
    val priceAlertSetup = TradeRoute("notifications/price-alert-setup", "Price Alert Setup", "Notifications", "D8.S07")

    // ---- Topic 9: Security & Identity ----
    val securityDashboard = TradeRoute("security/dashboard", "Security Dashboard / Overview", "Security & Identity", "D9.S01")
    val privacySettings = TradeRoute("security/privacy-settings", "Privacy Settings (incl. Pawns disclosure)", "Security & Identity", "D9.S17 / Blueprint 10.5")
    val guardianManagement = TradeRoute("security/guardians", "Social Recovery — Guardian Management", "Security & Identity", "D9.S11")
    val recoveryExecution = TradeRoute("security/recovery-execution", "Recovery Execution Flow", "Security & Identity", "D9.S12")
    val anomalyAlert = TradeRoute("security/anomaly-alert", "Anomaly Detection Alert", "Security & Identity", "D9.S16")
    val auditInfo = TradeRoute("security/audit-info", "Open-Source / Audit Info", "Security & Identity", "D9.S18")

    /** Every route above, in declaration order — the single source of truth for [TradeNavHost]. */
    val all: List<TradeRoute> = listOf(
        welcome, tutorial, termsOfService, onboardingHandoff,
        biometricPrompt, pinFallback, deviceBinding, passkeySetup, returningUserAuth,
        seedPhraseDisplay, socialRecoverySetup, sessionDeviceManagement,
        dashboard, assetDetail,
        pairSelector, tradingScreen, orderConfirmation, openOrders, tradeHistory,
        positionManagement, structuredProducts,
        walletHome, depositMethodPicker, depositAmountEntry, depositKorapayFlow,
        depositJuciwayFlow, depositResult, withdrawEntry, withdrawBiometricConfirm,
        withdrawBroadcastStatus, transactionHistory,
        aiAgentEntry, riskToleranceSelector, aiActivationConfirmation, agentStatusDashboard,
        signalFeed, signalDetail, backtestConfig, backtestResults,
        copyLeaderboard, traderProfile, copyAllocateFlow, activeCopiesDashboard, socialCopyFeed,
        p2pListingBrowse, p2pCreateListing, p2pEscrowBuyer, p2pEscrowSeller, p2pDisputeChat,
        earnProductList, earnDepositFlow, earnWithdrawFlow, earnActivePositions,
        notificationFeed, notificationDetail, notificationSettings, priceAlertSetup,
        securityDashboard, privacySettings, guardianManagement, recoveryExecution,
        anomalyAlert, auditInfo,
    )
}
