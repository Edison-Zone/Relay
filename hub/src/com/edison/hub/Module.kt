package com.edison.hub

import tinyb.BluetoothDevice
import tinyb.BluetoothGattCharacteristic
import tinyb.BluetoothGattService

/**
 * @author sergeys
 *
 * @constructor Creates a new Module
 */
data class Module(val device: BluetoothDevice, val service: BluetoothGattService,
                  val output: BluetoothGattCharacteristic,
                  val input: BluetoothGattCharacteristic) {
    
    var connected: Boolean = true
        private set
    
    fun close() {
        device.disconnect()
        service.close()
        output.close()
        input.close()
        device.close()
        connected = false
    }
}