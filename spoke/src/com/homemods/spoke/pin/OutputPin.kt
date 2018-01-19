package com.homemods.spoke.pin

/**
 * @author sergeys
 */
interface OutputPin {
    fun set(state: PinState)
    fun get(): PinState
    
    fun on() = set(PinState.ON)
    fun off() = set(PinState.OFF)
    
    fun toggle() = set(if (get() == PinState.ON) {
        PinState.OFF
    } else {
        PinState.ON
    })
    
    fun pulse(millis: Int)
    
    fun setShutdownState(state: PinState)
}