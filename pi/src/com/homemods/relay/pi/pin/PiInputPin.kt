package com.homemods.relay.pi.pin

import com.homemods.relay.pin.InputPin
import com.homemods.relay.pin.PinEdge
import com.homemods.relay.pin.PinState
import com.pi4j.io.gpio.GpioPinDigitalInput
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent
import com.pi4j.io.gpio.event.GpioPinListenerDigital

/**
 * @author sergeys
 */
class PiInputPin(val inputPin: GpioPinDigitalInput) : InputPin {
    override fun get(): PinState = inputPin.state.convert()
    
    override fun addListener(listener: (PinState, PinEdge) -> Unit) {
        inputPin.addListener(object : GpioPinListenerDigital {
            override fun handleGpioPinDigitalStateChangeEvent(event: GpioPinDigitalStateChangeEvent) {
                listener(event.state.convert(), event.edge.convert())
            }
        })
    }
}