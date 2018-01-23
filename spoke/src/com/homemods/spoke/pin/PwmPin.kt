package com.homemods.spoke.pin

/**
 * @author sergeys
 */
interface PwmPin {
    fun setPwm(value: Int)
    fun getPwm(): Int
}