@file:JvmName("Main")

package com.edison.hub

import com.edison.message.encrypted
import com.edison.message.formatMessage
import com.edison.message.pad
import java.io.InputStream
import java.net.Socket

/**
 * @author sergeys
 */

/*
 * CONSTANTS
 */
val modules = arrayOf<Module>(
        Module("HomeModesModule1", true),
        Module("HomeModesModule2", true))

val serviceUUID = "19B10000-E8F2-537E-4F6C-D104768A1214"

val dataSendUUID = "19B10000-E8F2-537E-4F6C-D104768A1214"
val dataReadUUID = "19B10001-E8F2-537E-4F6C-D104768A1214"

val serverIP = "128.61.105.54"
val serverPort = 11899

/*
 * PROGRAM DATA
 */

var socket: Socket? = null
var input: InputStream? = null

fun main(args: Array<String>) {
    
    //The code current always sends 16 byte long arrays over BLE
    
    Thread(::createBluetooth).apply {
        name = "Bluetooth Connection Thread"
        isDaemon = true
        start()
    }
    
    Thread(::createSocket).apply {
        name = "Server Socket Thread"
        isDaemon = true
        start()
    }
    
    while (input == null) {
        //Hub only requires server connection
        Thread.sleep(100)
    }
    
    val input = input!!
    
    val byteArray = ByteArray(3)
    byteArray[0] = 0x02 //Push next byte to the stack
    byteArray[1] = 0x00 //The data byte pushed to the stack
    byteArray[2] = 0x05 //Set variable 0 to the value given by the top value on the stack
    
    val byteArray2 = byteArrayOf(0x03, //Push next two bytes
                                 0x03, // See next
                                 0xE8.toByte(), // 0000 0011 1110 1000 = 1000
                                 0x0C, // Wait ms given by last two bytes on the stack
                                 0x02, //Push next byte
                                 0x7F, //127 or center position
                                 0x05) //Set variable 0 to the value given by the top value on the stack
    
    
    var msgId = 0
    
    val recieveBuffer = ByteArray(2)
    
    try {
        while (true) {
            //Read byte from input and copy to bluetooth message
            input.read(recieveBuffer)
            val moduleNum = recieveBuffer[0].toInt()
            
            println("Recieved from app for module $moduleNum")
            
            val m = modules[moduleNum]
    
            byteArray[1] = recieveBuffer[1]
    
            m.output?.writeValue(formatMessage(msgId++, byteArray).pad().encrypted())
    
            println("Sent to module")
            
            if (moduleNum == 0) {
                //Thread.sleep(1000)
                m.output?.writeValue(formatMessage(msgId++, byteArray2).pad().encrypted())
                println("Sent to module2")
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        //Don't ask me how the code will reach this unless it causes an exception
        closeAll()
    }
    
    System.exit(0)
}

fun createBluetooth() {
    BluetoothController.init()
    
    //Keep searching until we get the connections
    while (true) {
        if (!modules.all(Module::connected)) {
            //Start discovery
            BluetoothController.discovering = true
        
            modules.filter { m -> !m.connected }.forEach { m ->
                m.tryConnect()
            }
        } else {
            BluetoothController.discovering = false
        }
        //Try again after a second
        Thread.sleep(1000)
    }
}

fun createSocket() {
    println("Searching for server")
    socket = Socket(serverIP, serverPort)
    
    input = socket?.getInputStream()
    
    socket?.getOutputStream()?.write(0)
    println("Connected to server")
}

fun closeAll() {
    modules.forEach(Module::close)
    socket?.close()
    input?.close()
}
