@file:JvmName("Main")

package com.edison.hub

import com.edison.message.encrypted
import com.edison.message.formatMessage
import tinyb.BluetoothDevice
import java.io.InputStream
import java.net.Socket

/**
 * @author sergeys
 */

/*
 * CONSTANTS
 */
val USE_NAME = true

val deviceName1 = "HomeModesModule1"
val deviceAddress1 = ""

val deviceName2 = "HomeModesModule2"
val deviceAddress2 = ""

val serviceUUID = "19B10000-E8F2-537E-4F6C-D104768A1214"

val dataSendUUID = "19B10000-E8F2-537E-4F6C-D104768A1214"
val dataReadUUID = "19B10001-E8F2-537E-4F6C-D104768A1214"

val serverIP = "128.61.105.54"
val serverPort = 11899

/*
 * PROGRAM DATA
 */

var module1: Module? = null
var module2: Module? = null

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
    
    val byteArray = ByteArray(2)
    byteArray[0] = 0b0000 //Set motor 0 to next ubyte
    byteArray[1] = 0b01111111 //Position 0
    
    var msgId = 0
    
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
                module1
            } else { //motor 2
                module2
            }?.output?.writeValue(formatMessage(msgId++, byteArray).encrypted())
            
            byteArray[1] = 0b01111111
            Thread.sleep(1000)
            
            if (moduleNum == 0) { //motor 1
                module1
            } else { //motor 2
                module2
            }?.output?.writeValue(formatMessage(msgId++, byteArray).encrypted())
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
        if (module1 == null || module2 == null) {
            //Start discovery
            BluetoothController.discovering = true
            
            if (module1 == null) {
                val m = if (USE_NAME) {
                    BluetoothController.tryFindDeviceByName(deviceName1)
                } else {
                    BluetoothController.tryFindDeviceByAdd(deviceAddress1)
                }
                
                if (m != null) {
                    m.connect()
                    module1 = tryLoadDevice(m)
                }
            }
            
            if (module2 == null) {
                val m = if (USE_NAME) {
                    BluetoothController.tryFindDeviceByName(deviceName2)
                } else {
                    BluetoothController.tryFindDeviceByAdd(deviceAddress2)
                }
                
                if (m != null) {
                    m.connect()
                    module2 = tryLoadDevice(m)
                }
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
    module1?.close()
    module2?.close()
    socket?.close()
    input?.close()
}

fun tryLoadDevice(device: BluetoothDevice): Module? {
    while (!device.servicesResolved) {
        Thread.sleep(100)
    }
    
    val service = device.services.firstOrNull { s -> s.uuid == serviceUUID.toLowerCase() }
    
    if (service == null) {
        return null
    }
    
    val charSend = service.characteristics.firstOrNull { c -> c.uuid == dataSendUUID.toLowerCase() }
    val charRead = service.characteristics.firstOrNull { c -> c.uuid == dataReadUUID.toLowerCase() }
    
    if (charRead == null || charSend == null) {
        return null
    }
    
    return Module(device, charSend, charRead)
}