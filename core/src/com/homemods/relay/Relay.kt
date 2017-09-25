package com.homemods.relay

import com.homemods.relay.pin.PinFactory
import com.homemods.relay.pin.PinState
import java.util.Random

/**
 * @author sergeys
 */

class Relay(val pinFactory: PinFactory) {
    fun run() {
        val pin0 = pinFactory.createOutputPin(0)
        val rand = Random()
        
        pin0.setShutdownState(PinState.OFF)
        
        while (true) {
            pin0.toggle()
            Thread.sleep(rand.nextInt(1000).toLong())
        }
    }
}