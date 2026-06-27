package com.example.app.core.common.uistate

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal const val DEFAULT_STOP_TIMEOUT_MILLIS = 5_000L

@OptIn(ExperimentalCoroutinesApi::class)
private fun CoroutineScope.collectWhileSubscribed(
    subscriptionCount: StateFlow<Int>,
    stopTimeoutMillis: Long,
    block: suspend () -> Unit,
) {
    launch {
        subscriptionCount
            .map { it > 0 }
            .distinctUntilChanged()
            .flatMapLatest { active ->
                if (active) flowOf(true) else flow { delay(stopTimeoutMillis); emit(false) }
            }
            .distinctUntilChanged()
            .collectLatest { active -> if (active) block() }
    }
}

class UiStateHolder<S, U>(
    scope: CoroutineScope,
    initialUiState: U,
    source: StateFlow<S>,
    stopTimeoutMillis: Long = DEFAULT_STOP_TIMEOUT_MILLIS,
    private val transform: (S, U) -> U,
) {
    private val _uiState = MutableStateFlow(initialUiState)
    val state: StateFlow<U> = _uiState.asStateFlow()

    init {
        scope.collectWhileSubscribed(_uiState.subscriptionCount, stopTimeoutMillis) {
            source.collect { domain -> _uiState.update { current -> transform(domain, current) } }
        }
    }

    fun update(reduce: U.() -> U) { _uiState.update(reduce) }
}

class MultiSourceUiStateHolder<U>(
    scope: CoroutineScope,
    initialUiState: U,
    sources: List<Pair<StateFlow<*>, (Any?, U) -> U>>,
    stopTimeoutMillis: Long = DEFAULT_STOP_TIMEOUT_MILLIS,
) {
    private val _uiState = MutableStateFlow(initialUiState)
    val state: StateFlow<U> = _uiState.asStateFlow()

    init {
        if (sources.isNotEmpty()) {
            scope.collectWhileSubscribed(_uiState.subscriptionCount, stopTimeoutMillis) {
                coroutineScope {
                    sources.forEach { (src, merge) ->
                        launch {
                            @Suppress("UNCHECKED_CAST")
                            (src as StateFlow<Any?>).collect { domain ->
                                _uiState.update { current -> merge(domain, current) }
                            }
                        }
                    }
                }
            }
        }
    }

    fun update(reduce: U.() -> U) { _uiState.update(reduce) }
}

class UiStateHolderBuilder<S, U> {
    var scope: CoroutineScope? = null
    var initialState: U? = null
    var source: StateFlow<S>? = null
    var stopTimeoutMillis: Long = DEFAULT_STOP_TIMEOUT_MILLIS
    var transformBlock: ((S, U) -> U)? = null

    fun scope(s: CoroutineScope) = apply { scope = s }
    fun initialState(s: U) = apply { initialState = s }
    fun source(f: StateFlow<S>) = apply { source = f }
    fun stopTimeout(ms: Long) = apply { stopTimeoutMillis = ms }
    fun transform(b: (S, U) -> U) = apply { transformBlock = b }

    fun build() = UiStateHolder(
        scope          = checkNotNull(scope) { "scope must be set" },
        initialUiState = checkNotNull(initialState) { "initialState must be set" },
        source         = checkNotNull(source) { "source must be set" },
        stopTimeoutMillis = stopTimeoutMillis,
        transform      = checkNotNull(transformBlock) { "transform must be set" },
    )
}

class MultiSourceUiStateHolderBuilder<U> {
    var scope: CoroutineScope? = null
    var initialState: U? = null
    var stopTimeoutMillis: Long = DEFAULT_STOP_TIMEOUT_MILLIS
    val sources = mutableListOf<Pair<StateFlow<*>, (Any?, U) -> U>>()

    fun scope(s: CoroutineScope) = apply { scope = s }
    fun initialState(s: U) = apply { initialState = s }
    fun stopTimeout(ms: Long) = apply { stopTimeoutMillis = ms }

    @Suppress("UNCHECKED_CAST")
    fun <S> source(f: StateFlow<S>, m: (S, U) -> U) = apply {
        sources.add(f to (m as (Any?, U) -> U))
    }

    fun build() = MultiSourceUiStateHolder(
        scope          = checkNotNull(scope),
        initialUiState = checkNotNull(initialState),
        sources        = sources,
        stopTimeoutMillis = stopTimeoutMillis,
    )
}

fun <S, U> CoroutineScope.uiState(
    block: UiStateHolderBuilder<S, U>.() -> Unit
): UiStateHolder<S, U> = UiStateHolderBuilder<S, U>()
    .apply { scope(this@uiState) }
    .apply(block)
    .build()

fun <U> CoroutineScope.multiSourceUiState(
    block: MultiSourceUiStateHolderBuilder<U>.() -> Unit
): MultiSourceUiStateHolder<U> = MultiSourceUiStateHolderBuilder<U>()
    .apply { scope(this@multiSourceUiState) }
    .apply(block)
    .build()
