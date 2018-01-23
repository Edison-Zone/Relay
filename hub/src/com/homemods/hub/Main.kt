@file:JvmName("Main")

package com.homemods.hub

import com.homemods.bluetooth.BluetoothServer
import com.homemods.bluetooth.BluetoothServerSocket
import com.homemods.bluetooth.BluetoothSocket
import com.homemods.bluetooth.BluetoothStream
import com.homemods.message.encrypted
import com.homemods.message.formatMessage
import java.io.InputStream
import java.net.ServerSocket
import java.net.Socket

/**
 * @author sergeys
 */

var bluetoothServerSocket: BluetoothServerSocket? = null
var connection: BluetoothSocket? = null
var stream: BluetoothStream? = null

var serverSocket: ServerSocket? = null
var socket: Socket? = null
var input: InputStream? = null

fun main(args: Array<String>) {
    
    Thread(::createStream).apply {
        name = "Bluetooth Server Thread"
        isDaemon = true
        start()
    }
    
    Thread(::createSocket).apply {
        name = "Server Socket Thread"
        isDaemon = true
        start()
    }
    
    while (input == null && stream == null) {
        //Keep waiting until we have both connections
        Thread.sleep(100)
    }
    
    val stream = stream!!
    val input = input!!
    
    val byteArray = ByteArray(2)
    byteArray[0] = 0b0000 //Set motor 0 to next ubyte
    byteArray[1] = 0b0000 //Position 0
    
    var msgId = 0
    
    try {
        while (true) {
            //Read byte from input and copy to bluetooth message
            byteArray[1] = (input.read() and 0xFF).toByte()
            
            //Send it to the spoke
            stream.write(formatMessage(msgId++, byteArray).encrypted())
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        //Don't ask me how the code will reach this unless it causes an exception
        closeAll()
    }
}

fun createStream() {
    val bluetoothServer = BluetoothServer.native()
    
    bluetoothServerSocket = bluetoothServer.startServer(0x1001)
    
    bluetoothServerSocket?.listen()
    
    //Get a connection
    connection = bluetoothServerSocket?.acceptOneConnection()
    
    stream = connection?.createStream()
}

fun createSocket() {
    serverSocket = ServerSocket(11899)
    
    socket = serverSocket?.accept()
    
    input = socket?.getInputStream()
}

fun closeAll() {
    bluetoothServerSocket?.close()
    connection?.close()
    
    serverSocket?.close()
    socket?.close()
    input?.close()
}