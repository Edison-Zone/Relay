@file:JvmName("Main")

package com.homemods.hub

import com.homemods.bluetooth.BluetoothServer
import com.homemods.message.encrypted
import com.homemods.message.formatMessage
import kotlin.experimental.inv

/**
 * @author sergeys
 */
fun main(args: Array<String>) {
    val bluetoothServer = BluetoothServer.native()
    
    val bluetoothServerSocket = bluetoothServer.startServer(0x1001)
    
    bluetoothServerSocket.listen()
    
    //Get a connection
    val connection = bluetoothServerSocket.acceptOneConnection()
    
    val stream = connection.createStream()
    
    val byteArray = ByteArray(2)
    byteArray[0] = 0b0000 //Set motor 0 to next ubyte
    byteArray[1] = 0b0000 //Position 0
    
    var msgId = 0
    
    repeat(15) {
        val message = formatMessage(msgId++, byteArray)
        //Send the message
        println("Sending message $msgId")
        stream.write(message.encrypted())
        
        Thread.sleep(1000)
        
        byteArray[1] = byteArray[1].inv() //Swap from position 0 to 255
    }
    
    connection.close()
    bluetoothServerSocket.close()
}

