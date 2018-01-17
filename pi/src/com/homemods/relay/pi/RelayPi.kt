@file:JvmName("RelayPi")

package com.homemods.relay.pi

import com.homemods.relay.pi.bluetooth.BluetoothClient
import com.homemods.relay.pi.bluetooth.BluetoothServer

//Old Imports
/*
import de.serviceflow.codegenj.ObjectManager
import org.bluez.Adapter1
import java.io.IOException
import java.util.Arrays
import java.util.logging.Level
*/

/**
 * @author sergeys
 */

fun main(args: Array<String>) {
    
    //System.loadLibrary("bluetooth")
    System.loadLibrary("EdisonNative")
    
    if (args.contains("client")) { //Client code
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
    } else { //Server code
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
    
    //Begin Old Code
    //-------------------------------------------------------------------------------
    /*
    ObjectManager.getLogger().level = Level.INFO
    val objectManager = ObjectManager.getInstance()!!
    
    val adapters = objectManager.adapters!!
    
    var adapter: Adapter1? = null
    for (a in adapters) {
        try {
            a.startDiscovery()
            
            adapter = a
        } catch (e: IOException) {
            e.printStackTrace()
            println("Ignored Adapter")
        }
    }
    if (adapter == null) {
        System.err.println("No adapter found")
        System.exit(1)
        return
    }
    
    Thread.sleep(5000)
    
    for (a in adapters) {
        try {
            a.stopDiscovery()
        } catch (e: IOException) {
            //Ignore
        }
    }
    
    val device = adapter.devices.firstOrNull { d -> d.name == "HomeModesModule1" }
    
    if (device == null) {
        System.err.println("Failed to find device")
        System.exit(1)
        return
    }
    
    device.connect()
    
    print("Resolving services")
    while (!device.servicesResolved) {
        Thread.sleep(100)
        print(".")
    }
    println()
    
    val service = device.services.firstOrNull { s -> s.uuid == "19B10000-E8F2-537E-4F6C-D104768A1214".toLowerCase() }
    
    if (service == null) {
        System.err.println("Failed to find service")
        System.exit(1)
        return
    }
    
    val char = service.characteristics.firstOrNull()
    
    if (char == null) {
        System.err.println("Failed to find characteristic")
        System.exit(1)
        return
        
    }
    
    val bytes = ByteArray(1)
    bytes[0] = 123
    
    println("Flags: ${Arrays.toString(char.flags)}")
    
    char.writeValue(bytes)
    
    println(char.readValue())
    
    device.disconnect()
    */
    //-------------------------------------------------------------------------------
    //End Old Code
    
    //    Relay(PiPinFactory(), PiBluetoothConnectionFactory()).run()
}