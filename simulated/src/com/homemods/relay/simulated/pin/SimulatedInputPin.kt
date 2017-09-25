package com.homemods.relay.simulated.pin

import com.homemods.relay.pin.InputPin
import com.homemods.relay.pin.PinEdge
import com.homemods.relay.pin.PinState

/**
 * @author sergeys
 */
class SimulatedInputPin : InputPin {
    var state = false
    val listeners = arrayListOf<(PinState, PinEdge) -> Unit>()
    
    override fun get(): PinState {
        return if (state) {
            PinState.ON
        } else {
            PinState.OFF
        }
    }
    
    override fun addListener(listener: (PinState, PinEdge) -> Unit) {
        listeners.add(listener)
    }
}