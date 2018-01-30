package com.edison.hub

import tinyb.BluetoothDevice
import tinyb.BluetoothGattCharacteristic

/**
 * @author sergeys
 *
 * @constructor Creates a new Module
 */
data class Module(val device: BluetoothDevice, val output: BluetoothGattCharacteristic,
                  val input: BluetoothGattCharacteristic) {
    
    var connected: Boolean = true
        private set
    
    fun close() {
        device.close()
        connected = false
    }
}