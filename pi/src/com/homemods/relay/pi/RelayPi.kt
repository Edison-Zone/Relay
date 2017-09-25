@file:JvmName("RelayPi")

package com.homemods.relay.pi

import com.homemods.relay.Relay
import com.homemods.relay.pi.pin.PiPinFactory

/**
 * @author sergeys
 */

fun main(args: Array<String>) {
    Relay(PiPinFactory()).run()
}