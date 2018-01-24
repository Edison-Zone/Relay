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
var connection2: BluetoothSocket? = null
var stream: BluetoothStream? = null
var stream2: BluetoothStream? = null

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
    
    while (input == null || stream == null || stream2 == null) {
        //Keep waiting until we have all connections
        Thread.sleep(100)
    }
    
    val stream = stream!!
    val stream2 = stream2!!
    val input = input!!
    
    val byteArray = ByteArray(2)
    byteArray[0] = 0b0000 //Set motor 0 to next ubyte
    byteArray[1] = 0b01111111 //Position 0
    
    var msgId = 0
    
    stream.write(formatMessage(msgId++, byteArray).encrypted())
    stream2.write(formatMessage(msgId++, byteArray).encrypted())
    
    val recieveBuffer = ByteArray(2)
    
    try {
        while (true) {
            //Read byte from input and copy to bluetooth message
            input.read(recieveBuffer)
            val moduleNum = recieveBuffer[0].toInt()
    
            println("Recieved from app for module $moduleNum")
    
            byteArray[1] = recieveBuffer[1]
    
            //Send it to the spoke
            if (moduleNum == 0) { //motor 1
                stream
            } else { //motor 2
                stream2
            }.write(formatMessage(msgId++, byteArray).encrypted())
    
            byteArray[1] = 0b01111111
            Thread.sleep(1000)
    
            if (moduleNum == 0) { //motor 1
                stream
            } else { //motor 2
                stream2
            }.write(formatMessage(msgId++, byteArray).encrypted())
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
    
    //Get a connection
    connection2 = bluetoothServerSocket?.acceptOneConnection()
    
    stream2 = connection2?.createStream()
}

fun createSocket() {
    serverSocket = ServerSocket(11899)
    
    socket = serverSocket?.accept()
    
    input = socket?.getInputStream()
    
    println("Connected to ${socket?.localAddress?.hostAddress}")
}

fun closeAll() {
    bluetoothServerSocket?.close()
    connection?.close()
    connection2?.close()
    
    serverSocket?.close()
    socket?.close()
    input?.close()
}