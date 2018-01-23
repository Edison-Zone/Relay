package com.homemods.spoke.pin

import com.pi4j.io.gpio.GpioPinPwmOutput

/**
 * @author sergeys
 */
class PiPwmPin(val pwmPin: GpioPinPwmOutput) : PwmPin {
    override fun setPwm(value: Int) {
        pwmPin.pwm = value
    }
    
    override fun getPwm(): Int = pwmPin.pwm
}