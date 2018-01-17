package com.homemods.relay.pi.bluetooth

/**
 * @author sergeys
 */
class BluetoothClient(private val impl: Impl) {
    
    fun startClient() = impl.createSocket()
    
    interface Impl {
        fun createSocket(): BluetoothClientSocket
    }
    
    companion object {
        fun native() = BluetoothClient(BluetoothClientNativeImpl())
    }
}