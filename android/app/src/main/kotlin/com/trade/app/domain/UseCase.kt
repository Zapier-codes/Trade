package com.trade.app.domain

/**
 * Base contract for a domain use case that takes parameters. Every use
 * case in every layer (this module and, once they exist, feature
 * modules) should follow this shape — a single suspend `invoke`, callable
 * as `useCase(params)`.
 */
fun interface UseCase<in Params, out Result> {
    suspend operator fun invoke(params: Params): Result
}

/** Base contract for a domain use case that takes no parameters. */
fun interface NoParamsUseCase<out Result> {
    suspend operator fun invoke(): Result
}
