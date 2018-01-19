package com.homemods.bluetooth

/**
 * @author sergeys
 *
 * @constructor Creates a new BluetoothServerSocket
 */
class BluetoothServerSocket(private val socket: Int, private val impl: Impl) {
    
    fun listen() = impl.listen(socket)
    
    fun acceptOneConnection() = impl.acceptConnection(socket)
    
    fun close() = impl.close(socket)
    
    interface Impl {
        fun listen(socket: Int)
        
        fun acceptConnection(socket: Int): BluetoothSocket
        
        fun close(socket: Int)
    }
}