package com.edison.hub

import tinyb.BluetoothDevice
import tinyb.BluetoothException
import tinyb.BluetoothGattCharacteristic
import tinyb.BluetoothGattService

/**
 * @author sergeys
 *
 * @constructor Creates a new Module
 */
data class Module(val id: String, val isName: Boolean) {
    
    var device: BluetoothDevice? = null
    var service: BluetoothGattService? = null
    var output: BluetoothGattCharacteristic? = null
    var input: BluetoothGattCharacteristic? = null
    
    var connected: Boolean = false
        private set
    
    var ready: Boolean = false
        private set
    
    fun tryConnect() {
        if (!connected) {
            try {
                val d = if (isName) {
                    BluetoothController.tryFindDeviceByName(id)
                } else {
                    BluetoothController.tryFindDeviceByAdd(id)
                }
        
                if (d != null) {
                    device = d
                    d.connect()
            
                    connected = true
            
                    tryLoadDevice(d)
            
                    println("Connected to modules")
                }
            } catch (e: BluetoothException) {
                close()
            }
        }
    }
    
    fun close() {
        if (connected) {
            if (device?.connected == true) {
                device?.disconnect()
            }
            service?.close()
            output?.close()
            input?.close()
            device?.close()
        
            connected = false
            ready = false
        
            device = null
            service = null
            output = null
            input = null
        }
    }
    
    private fun tryLoadDevice(device: BluetoothDevice) {
        while (!device.servicesResolved) {
            Thread.sleep(100)
        }
        
        val service = device.services.firstOrNull { s -> s.uuid == serviceUUID.toLowerCase() }
        
        if (service == null) {
            close()
            return
        }
        
        val charSend = service.characteristics.firstOrNull { c -> c.uuid == dataSendUUID.toLowerCase() }
        val charRead = service.characteristics.firstOrNull { c -> c.uuid == dataReadUUID.toLowerCase() }
        
        if (charRead == null || charSend == null) {
            close()
            return
        }
        
        this.service = service
        this.output = charSend
        this.input = charRead
        ready = true
    }
}
