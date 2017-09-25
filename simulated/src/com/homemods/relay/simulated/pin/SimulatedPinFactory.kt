package com.homemods.relay.simulated.pin

import com.homemods.relay.pin.InputPin
import com.homemods.relay.pin.OutputPin
import com.homemods.relay.pin.PinAlreadyBoundException
import com.homemods.relay.pin.PinDoesNotExistException
import com.homemods.relay.pin.PinFactory
import com.homemods.relay.pin.PinResistance
import com.homemods.relay.pin.PinState
import java.util.concurrent.Executors

/**
 * @author sergeys
 */
class SimulatedPinFactory : PinFactory {
    val pins = arrayOfNulls<Any>(32)
    val executorService = Executors.newSingleThreadExecutor()
    
    lateinit var onPinUpdate: () -> Unit
    
    override fun createInputPin(pin: Int, pinResistance: PinResistance, pinName: String?): InputPin {
        if (pin > 31 || pin < 0) throw PinDoesNotExistException("Pin $pin does not exist")
        
        if (pins[pin] == null) {
            pins[pin] = SimulatedInputPin()
        } else {
            throw PinAlreadyBoundException("Pin $pin already bound to a ${pins[pin]!!::class.java.simpleName}")
        }
        
        return pins[pin] as InputPin
    }
    
    override fun createOutputPin(pin: Int, defaultState: PinState, pinName: String?): OutputPin {
        if (pin > 31 || pin < 0) throw PinDoesNotExistException("Pin $pin does not exist")
        
        if (pins[pin] == null) {
            pins[pin] = SimulatedOutputPin(onPinUpdate, { time ->
                executorService.submit {
                    Thread.sleep(time.toLong())
                    (pins[pin] as SimulatedOutputPin).set(PinState.OFF)
                }
            })
        } else {
            throw PinAlreadyBoundException("Pin $pin already bound to a ${pins[pin]!!::class.java.simpleName}")
        }
        
        return pins[pin] as OutputPin
    }
    
    operator fun get(idx: Int) = pins[idx]
}