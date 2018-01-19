package com.homemods.spoke.pin

/**
 * @author sergeys
 */

typealias PiPinState = com.pi4j.io.gpio.PinState
typealias PiPinEdge = com.pi4j.io.gpio.PinEdge

fun PinState.convert() = if (this == PinState.ON) {
    PiPinState.HIGH
} else {
    PiPinState.LOW
}

fun PiPinState.convert() = if (this == PiPinState.HIGH) {
    PinState.ON
} else {
    PinState.OFF
}

fun PinEdge.convert() = when (this) {
    PinEdge.FALLING -> PiPinEdge.FALLING
    PinEdge.RISING  -> PiPinEdge.RISING
    PinEdge.NONE    -> PiPinEdge.NONE
    PinEdge.BOTH    -> PiPinEdge.BOTH
}

fun PiPinEdge.convert() = when (this) {
    PiPinEdge.FALLING -> PinEdge.FALLING
    PiPinEdge.RISING  -> PinEdge.RISING
    PiPinEdge.NONE    -> PinEdge.NONE
    PiPinEdge.BOTH    -> PinEdge.BOTH
}
