@file:JvmName("Main")

package com.homemods.hub

import com.homemods.bluetooth.BluetoothServer

/**
 * @author sergeys
 */
fun main(args: Array<String>) {
    val bluetoothServer = BluetoothServer.native()
    
    val bluetoothServerSocket = bluetoothServer.startServer(0x1001)
    
    bluetoothServerSocket.listen()
    
    val connection = bluetoothServerSocket.acceptOneConnection()
    
    val bytes = ByteArray(1024) { i -> 10 }
    
    connection.write(bytes, 64)
    
    val recieved = ByteArray(1024)
    
    val count = connection.read(recieved, 1024)
    
    for (i in 0 until count) {
        println("${bytes[i]} -> ${recieved[i]}")
    }
    
    connection.close()
    bluetoothServerSocket.close()
}

