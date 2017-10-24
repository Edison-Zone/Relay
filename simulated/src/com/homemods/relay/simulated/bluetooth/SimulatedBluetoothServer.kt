package com.homemods.relay.simulated.bluetooth

import com.homemods.relay.bluetooth.BluetoothConnection
import com.homemods.relay.bluetooth.BluetoothServer

/**
 * @author sergeys
 */
class SimulatedBluetoothServer : BluetoothServer {
    
    var discoveryCallback: ((BluetoothConnection) -> Unit)? = null
        private set
    
    override fun beginDiscovery(onDiscovery: (BluetoothConnection) -> Unit) {
        discoveryCallback = onDiscovery
    }
    
    override fun endDiscovery() {
        discoveryCallback = null
    }
}