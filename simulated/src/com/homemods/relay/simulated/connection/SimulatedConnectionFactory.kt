package com.homemods.relay.simulated.connection

import com.homemods.relay.connection.ClientConnection
import com.homemods.relay.connection.ConnectionFactory

/**
 * @author sergeys
 */
class SimulatedConnectionFactory : ConnectionFactory {
    
    var discoveryCallback: ((ClientConnection) -> Unit)? = null
        private set
    
    override fun beginDiscovery(onDiscovery: (ClientConnection) -> Unit) {
        discoveryCallback = onDiscovery
    }
    
    override fun endDiscovery() {
        discoveryCallback = null
    }
}