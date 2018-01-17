package com.homemods.relay.pi.bluetooth

/**
 * @author sergeys
 */
class BluetoothClientSocket(socket: Int, private val impl: Impl) : BluetoothSocket(socket, impl) {
    
    fun connect(address: String, port: Int): BluetoothSocket {
        impl.connect(socket, address, port)
        return this
    }
    
    interface Impl : BluetoothSocket.Impl {
        fun connect(socket: Int, address: String, port: Int): Int
    }
}