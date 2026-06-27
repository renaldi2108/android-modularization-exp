package com.example.app.core.utils.viewmodel

import com.example.app.core.common.uistate.MultiSourceUiStateHolderBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BaseViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    private data class CounterState(val count: Int = 0, val label: String = "")

    private sealed interface CounterAction {
        data class SetLabel(val value: String) : CounterAction
    }

    private class CounterViewModel(
        private val countFlow: StateFlow<Int> = MutableStateFlow(0),
    ) : BaseViewModel<CounterState, CounterAction>() {
        override fun initialState() = CounterState()

        override fun MultiSourceUiStateHolderBuilder<CounterState>.setupHolder() {
            source(countFlow) { domain, current -> current.copy(count = domain) }
        }

        override fun onAction(action: CounterAction) = when (action) {
            is CounterAction.SetLabel -> updateUi { copy(label = action.value) }
        }
    }

    @Before
    fun setUp() { Dispatchers.setMain(dispatcher) }

    @After
    fun tearDown() { Dispatchers.resetMain() }

    @Test
    fun `uiState diawali dengan initialState`() {
        val vm = CounterViewModel()
        assertEquals(CounterState(), vm.uiState.value)
    }

    @Test
    fun `updateUi lewat onAction memperbarui state`() {
        val vm = CounterViewModel()
        vm.onAction(CounterAction.SetLabel("halo"))
        assertEquals("halo", vm.uiState.value.label)
    }

    @Test
    fun `source memperbarui state saat di-collect`() = runTest(dispatcher) {
        val flow = MutableStateFlow(0)
        val vm = CounterViewModel(flow)
        backgroundScope.launch { vm.uiState.collect {} }
        flow.value = 7
        advanceUntilIdle()
        assertEquals(7, vm.uiState.value.count)
    }
}
