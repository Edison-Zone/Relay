@file:JvmName("Main")

package com.edison.hub

import com.edison.message.encrypted
import com.edison.message.formatMessage
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
    
    var msgId = 0
    
    val recieveBuffer = ByteArray(2)
    
    try {
        while (true) {
            //Read byte from input and copy to bluetooth message
            input.read(recieveBuffer)
            val moduleNum = recieveBuffer[0].toInt()
            
            println("Recieved from app for module $moduleNum")
            
            byteArray[1] = recieveBuffer[1]
    
            val m = modules[moduleNum]
    
            m.output?.writeValue(formatMessage(msgId++, byteArray).encrypted())
    
            if (moduleNum == 0) {
                byteArray[1] = 0b01111111
                Thread.sleep(1000)
        
                m.output?.writeValue(formatMessage(msgId++, byteArray).encrypted())
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        //Don't ask me how the code will reach this unless it causes an exception
        closeAll()
    }
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
        
        break
    }
}

fun createSocket() {
    socket = Socket(serverIP, serverPort)
    
    input = socket?.getInputStream()
}

fun closeAll() {
    modules.forEach(Module::close)
    socket?.close()
    input?.close()
}
