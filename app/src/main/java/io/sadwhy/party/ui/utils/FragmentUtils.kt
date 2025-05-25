package io.sadwhy.party.ui.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Extension function for Fragment to observe regular Flow with lifecycle awareness.
 * 
 * @param flow The Flow to observe
 * @param minActiveState Minimum lifecycle state when collection should be active (default: STARTED)
 * @param onValue Callback invoked when flow emits a value
 */
inline fun <T> Fragment.observeFlow(
    flow: Flow<T>,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline onValue: (T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(minActiveState) {
            flow.collect { value ->
                onValue(value)
            }
        }
    }
}

/**
 * Extension function for Fragment to observe StateFlow with lifecycle awareness.
 * StateFlow holds current state and emits immediately to new collectors.
 * 
 * @param stateFlow The StateFlow to observe
 * @param minActiveState Minimum lifecycle state when collection should be active (default: STARTED)
 * @param onStateChanged Callback invoked when state changes
 */
inline fun <T> Fragment.observeStateFlow(
    stateFlow: StateFlow<T>,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline onStateChanged: (T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(minActiveState) {
            stateFlow.collect { state ->
                onStateChanged(state)
            }
        }
    }
}

/**
 * Extension function for Fragment to observe SharedFlow with lifecycle awareness.
 * SharedFlow is used for one-time events like navigation, toasts, etc.
 * 
 * @param sharedFlow The SharedFlow to observe
 * @param minActiveState Minimum lifecycle state when collection should be active (default: STARTED)
 * @param onEvent Callback invoked when flow emits an event
 */
inline fun <T> Fragment.observeSharedFlow(
    sharedFlow: SharedFlow<T>,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline onEvent: (T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(minActiveState) {
            sharedFlow.collect { event ->
                onEvent(event)
            }
        }
    }
}

/**
 * Extension function for Fragment to observe Flow only when Fragment is in RESUMED state.
 * Useful for flows that should only be active when the fragment is fully visible.
 * 
 * @param flow The Flow to observe
 * @param onValue Callback invoked when flow emits a value
 */
inline fun <T> Fragment.observeFlowWhenResumed(
    flow: Flow<T>,
    crossinline onValue: (T) -> Unit
) {
    observeFlow(flow, Lifecycle.State.RESUMED, onValue)
}

/**
 * Extension function for Fragment to observe StateFlow only when Fragment is in RESUMED state.
 * 
 * @param stateFlow The StateFlow to observe
 * @param onStateChanged Callback invoked when state changes
 */
inline fun <T> Fragment.observeStateFlowWhenResumed(
    stateFlow: StateFlow<T>,
    crossinline onStateChanged: (T) -> Unit
) {
    observeStateFlow(stateFlow, Lifecycle.State.RESUMED, onStateChanged)
}

/**
 * Extension function for Fragment to observe SharedFlow only when Fragment is in RESUMED state.
 * 
 * @param sharedFlow The SharedFlow to observe
 * @param onEvent Callback invoked when flow emits an event
 */
inline fun <T> Fragment.observeSharedFlowWhenResumed(
    sharedFlow: SharedFlow<T>,
    crossinline onEvent: (T) -> Unit
) {
    observeSharedFlow(sharedFlow, Lifecycle.State.RESUMED, onEvent)
}

/**
 * Extension function for Fragment to observe Flow only when Fragment is in CREATED state.
 * Useful for flows that should be active as soon as the fragment is created.
 * 
 * @param flow The Flow to observe
 * @param onValue Callback invoked when flow emits a value
 */
inline fun <T> Fragment.observeFlowWhenCreated(
    flow: Flow<T>,
    crossinline onValue: (T) -> Unit
) {
    observeFlow(flow, Lifecycle.State.CREATED, onValue)
}