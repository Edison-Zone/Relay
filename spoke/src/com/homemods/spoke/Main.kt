@file:JvmName("Main")

package com.homemods.spoke

import com.homemods.bluetooth.BluetoothClient

/**
 * @author sergeys
 */

fun main(args: Array<String>) {
    val bluetoothClient = BluetoothClient.native()
    
    val bluetoothClientSocket = bluetoothClient.startClient()
    
    //Ports are odd numbers in the range from 0x1001 to 0x8FFF
    val bluetoothSocket = bluetoothClientSocket.connect("B8:27:EB:0E:5F:5F", 0x1001)
    
    assert(bluetoothClientSocket == bluetoothSocket)
    
    val bytes = ByteArray(1024)
    
    val count = bluetoothSocket.read(bytes, 1024)
    
    for (i in 0 until count) {
        println(bytes[i])
        bytes[i]++
    }
    
    bluetoothSocket.write(bytes, count)
    
    bluetoothSocket.close()
}