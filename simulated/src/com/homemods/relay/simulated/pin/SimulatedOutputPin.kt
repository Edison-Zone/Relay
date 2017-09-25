package com.homemods.relay.simulated.pin

import com.homemods.relay.pin.OutputPin
import com.homemods.relay.pin.PinState

/**
 * @author sergeys
 */
class SimulatedOutputPin(val onPinUpdate: () -> Unit, val pulseReciever: (Int) -> Unit) : OutputPin {
    
    private var state: Boolean = false
        set(value) {
            field = value
            onPinUpdate()
        }
    
    override fun set(state: PinState) {
        this.state = state == PinState.ON
    }
    
    override fun get(): PinState {
        return if (state) {
            PinState.ON
        } else {
            PinState.OFF
        }
    }
    
    override fun pulse(millis: Int) {
        state = true
        pulseReciever(millis)
    }
    
    override fun setShutdownState(state: PinState) {
        //Doesn't need to do anything in simulation
    }
    
}