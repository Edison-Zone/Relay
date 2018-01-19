package com.homemods.spoke.pin

import com.pi4j.io.gpio.GpioPinDigitalOutput

/**
 * @author sergeys
 */
class PiOutputPin(val outputPin: GpioPinDigitalOutput) : OutputPin {
    private var currState = PinState.OFF
    
    override fun set(state: PinState) {
        currState = state
        outputPin.state = state.convert()
    }
    
    override fun get(): PinState = currState
    
    override fun pulse(millis: Int) {
        outputPin.pulse(millis.toLong())
    }
    
    override fun setShutdownState(state: PinState) {
        outputPin.setShutdownOptions(true, state.convert())
    }
}