package com.homemods.relay.pi.bluetooth

/**
 * @author sergeys
 *
 * @constructor Creates a new BluetoothServer
 */
class BluetoothServer(private val impl: BluetoothServer.Impl) {
    fun startServer(port: Int) = impl.createSocket(port)
    
    interface Impl {
        fun createSocket(port: Int): BluetoothServerSocket
    }
    
    companion object {
        fun native() = BluetoothServer(BluetoothServerNativeImpl())
    }
}