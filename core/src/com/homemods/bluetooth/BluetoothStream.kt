package com.homemods.bluetooth

import kotlin.math.min

/**
 * @author sergeys
 */
class BluetoothStream(private val socket: BluetoothSocket) {
    private var incomingPosition = 0
    private var incomingLength = 0
    private val incomingBuffer = ByteArray(1024)
    private val outgoingBuffer = ByteArray(1024)
    
    fun write(bytes: ByteArray) {
        outgoingBuffer[0] = ((bytes.size and 0xFF000000L.toInt()) ushr 24).toByte()
        outgoingBuffer[1] = ((bytes.size and 0x00FF0000L.toInt()) ushr 16).toByte()
        outgoingBuffer[2] = ((bytes.size and 0x0000FF00L.toInt()) ushr 8).toByte()
        outgoingBuffer[3] = (bytes.size and 0x000000FFL.toInt()).toByte()
        var size = 4
        
        var pos = min(bytes.size, outgoingBuffer.size - size)
        
        System.arraycopy(bytes, 0, outgoingBuffer, size, pos)
        size += pos
        
        socket.write(outgoingBuffer, size)
        
        while (pos < bytes.size) {
            val inc = min(bytes.size - pos, outgoingBuffer.size)
            System.arraycopy(bytes, pos, outgoingBuffer, 0, inc)
            pos += inc
            
            socket.write(bytes, inc)
        }
        
    }
    
    fun read(): ByteArray {
        if (incomingPosition >= incomingLength) {
            incomingPosition = 0
            incomingLength = socket.read(incomingBuffer, 1024)
        }
        
        val size = (incomingBuffer[incomingPosition++].toInt() shl 24) or (
                incomingBuffer[incomingPosition++].toInt() shl 16) or (
                incomingBuffer[incomingPosition++].toInt() shl 8) or (
                incomingBuffer[incomingPosition++].toInt())
        
        val bytes = ByteArray(size)
        
        var position = min(size, incomingLength - incomingPosition)
        System.arraycopy(incomingBuffer, incomingPosition, bytes, 0, position)
        incomingPosition += position
        
        while (position < size) {
            if (incomingPosition >= incomingLength) {
                incomingPosition = 0
                incomingLength = socket.read(incomingBuffer, 1024)
            }
            
            val copySize = min(size, incomingLength - incomingPosition)
            System.arraycopy(incomingBuffer, incomingPosition, bytes, position, copySize)
            position += copySize
            incomingPosition += copySize
        }
        
        return bytes
    }
}