package com.homemods.bluetooth

/**
 * @author sergeys
 *
 * @constructor Creates a new BluetoothSocket
 */
open class BluetoothSocket(protected val socket: Int, private val impl: Impl) {
    
    fun read(bytes: ByteArray, count: Int): Int = impl.read(socket, bytes, count)
    
    fun write(bytes: ByteArray, count: Int) {
        impl.write(socket, bytes, count)
    }
    
    fun close() = impl.close(socket)
    
    fun createStream() = BluetoothStream(this)
    
    interface Impl {
        fun read(socket: Int, bytes: ByteArray, count: Int): Int
        
        fun write(socket: Int, bytes: ByteArray, count: Int): Int
        
        fun close(socket: Int)
    }
}