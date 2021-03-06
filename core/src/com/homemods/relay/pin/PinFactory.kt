package com.homemods.relay.pin

/**
 * @author sergeys
 */
interface PinFactory {
    fun createInputPin(pin: Int, pinResistance: PinResistance = PinResistance.PULL_UP,
                       pinName: String? = null): InputPin
    
    fun createOutputPin(pin: Int, defaultState: PinState = PinState.OFF, pinName: String? = null): OutputPin
}