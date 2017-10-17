package com.homemods.relay

import com.homemods.relay.pin.OutputPin
import com.homemods.relay.pin.PinFactory
import com.homemods.relay.pin.PinState
import java.util.Random

/**
 * @author sergeys
 */

class Relay(val pinFactory: PinFactory) {
    fun run() {
        val pins = Array<OutputPin>(8) { pinNum -> pinFactory.createOutputPin(pinNum) }
        pins.forEach { pin ->
            pin.setShutdownState(PinState.OFF)
        }
        
        val rand = Random()
        val bytes = ByteArray(1)
        var num: Int
        
        while (true) {
            rand.nextBytes(bytes)
            num = bytes[0].toInt()
    
            for (i in 0..7) {
                num = num ushr 1
                pins[i].set(if (num and 0b0000_0001 != 0) {
                    PinState.ON
                } else {
                    PinState.OFF
                })
            }
    
            Thread.sleep(1000)
        }
    }
}

