package com.homemods.relay.bluetooth

/**
 * @author sergeys
 */
interface BluetoothServer {
    fun beginDiscovery(onDiscovery: (BluetoothConnection) -> Unit)
    fun endDiscovery()
}
