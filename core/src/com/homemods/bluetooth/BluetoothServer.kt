package com.homemods.bluetooth

/**
 * @author sergeys
 *
 * @constructor Creates a new BluetoothServer
 */
class BluetoothServer(private val impl: Impl) {
    fun startServer(port: Int) = impl.createSocket(port)
    
    interface Impl {
        fun createSocket(port: Int): BluetoothServerSocket
    }
    
    companion object {
        fun native(): BluetoothServer {
            loadNativesIfNeeded()
            return BluetoothServer(BluetoothServerNativeImpl())
        }
    }
}