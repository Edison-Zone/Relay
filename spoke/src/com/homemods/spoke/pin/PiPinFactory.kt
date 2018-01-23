package com.homemods.spoke.pin

import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.PinPullResistance
import com.pi4j.io.gpio.RaspiPin

/**
 * @author sergeys
 */
class PiPinFactory : PinFactory {
    val pins = arrayOfNulls<Any>(32)
    val gpio = GpioFactory.getInstance()
    
    init {
        Runtime.getRuntime().addShutdownHook(Thread {
            gpio.shutdown()
        })
    }
    
    override fun createInputPin(pin: Int, pinResistance: PinResistance, pinName: String?): InputPin {
        if (pin > 31 || pin < 0) throw PinDoesNotExistException("Pin $pin does not exist")
        
        if (pins[pin] == null) {
            val inputPin = gpio.provisionDigitalInputPin(RaspiPin.getPinByAddress(pin), pinName, if (pinResistance == PinResistance.PULL_UP) {
                PinPullResistance.PULL_UP
            } else {
                PinPullResistance.PULL_DOWN
            })
            pins[pin] = PiInputPin(inputPin)
        } else {
            throw PinAlreadyBoundException("Pin $pin already bound to a ${pins[pin]!!::class.java.simpleName}")
        }
        
        return pins[pin] as InputPin
    }
    
    override fun createOutputPin(pin: Int, defaultState: PinState, pinName: String?): OutputPin {
        if (pin > 31 || pin < 0) throw PinDoesNotExistException("Pin $pin does not exist")
        
        if (pins[pin] == null) {
            val outputPin = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(pin), pinName, defaultState.convert())
            pins[pin] = PiOutputPin(outputPin)
        } else {
            throw PinAlreadyBoundException("Pin $pin already bound to a ${pins[pin]!!::class.java.simpleName}")
        }
        
        return pins[pin] as OutputPin
    }
    
    override fun createPwmPin(pin: Int, pinName: String?, defaultValue: Int?): PwmPin {
        if (pin > 31 || pin < 0) throw PinDoesNotExistException("Pin $pin does not exist")
        
        
        if (pins[pin] == null) {
            val pwmPin = if (pinName == null) {
                gpio.provisionPwmOutputPin(RaspiPin.getPinByAddress(pin))
            } else if (defaultValue == null) {
                gpio.provisionPwmOutputPin(RaspiPin.getPinByAddress(pin), pinName)
            } else {
                gpio.provisionPwmOutputPin(RaspiPin.getPinByAddress(pin), pinName, defaultValue)
            }
            pins[pin] = PiPwmPin(pwmPin)
        } else {
            throw PinAlreadyBoundException("Pin $pin already bound to a ${pins[pin]!!::class.java.simpleName}")
        }
        
        return pins[pin] as PwmPin
    }
}