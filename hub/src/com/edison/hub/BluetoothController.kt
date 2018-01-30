package com.edison.hub

import tinyb.BluetoothManager

/**
 * @author sergeys
 */
object BluetoothController {
    lateinit var bluetoothManager: BluetoothManager
    
    var discovering: Boolean = false
        set(value) {
            if (value != field) {
                if (discovering) {
                    bluetoothManager.startDiscovery()
                } else {
                    bluetoothManager.stopDiscovery()
                }
                field = value
            }
        }
    
    fun init() {
        bluetoothManager = BluetoothManager.getBluetoothManager()!!
    }
    
    fun tryFindDeviceByName(name: String) = bluetoothManager.devices.firstOrNull { d -> d.name == name }
    
    fun tryFindDeviceByAdd(address: String) = bluetoothManager.devices.firstOrNull { d -> d.address == address }
}