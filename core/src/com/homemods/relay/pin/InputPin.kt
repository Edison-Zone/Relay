package com.homemods.relay.pin

/**
 * @author sergeys
 */
interface InputPin {
    fun get(): PinState
    
    fun isOn() = get() == PinState.ON
    fun isOff() = get() == PinState.OFF
    
    fun addListener(listener: (PinState, PinEdge) -> Unit)
}