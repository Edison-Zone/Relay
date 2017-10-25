package com.homemods.relay.pi.bluetooth

import com.homemods.relay.bluetooth.BluetoothConnection
import com.homemods.relay.bluetooth.BluetoothServer
import javax.bluetooth.DiscoveryAgent
import javax.bluetooth.LocalDevice
import javax.bluetooth.UUID
import javax.microedition.io.Connector
import javax.microedition.io.StreamConnectionNotifier

/**
 * @author sergeys
 */
class PiBluetoothServer : BluetoothServer {
    private var running: Boolean = true
    private var discoveryThread: Thread? = null
    
    override fun beginDiscovery(onDiscovery: (BluetoothConnection) -> Unit) {
        if (discoveryThread != null) throw Exception("Server already in discovery mode")
        
        running = false
        discoveryThread = Thread(
                {
                    val localDevice = LocalDevice.getLocalDevice()!!
                    localDevice.discoverable = DiscoveryAgent.GIAC
                
                    val uuid = UUID("9fb18ac2a4fd865a79163c954fa189bf", false)
                    println(uuid)
                
                    val url = "btspp://localhost:$uuid;name=RemoteBluetooth"
                
                    val streamConnectionNotifier = Connector.open(url) as StreamConnectionNotifier
                
                    while (running) {
                        val connection = streamConnectionNotifier.acceptAndOpen()!!
                        onDiscovery(PiBluetoothConnection(connection))
                    }
                
                    streamConnectionNotifier.close()
                }, "Bluetooth Discovery Thread").apply {
            isDaemon = true
            start()
        }
    }
    
    override fun endDiscovery() {
        if (discoveryThread == null) throw Exception("Server not in discovery mode")
        running = false
        discoveryThread?.apply {
            @Suppress("DEPRECATION")
            stop()
        }
    }
}
