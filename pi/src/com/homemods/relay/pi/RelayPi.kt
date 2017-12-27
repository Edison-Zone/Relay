@file:JvmName("RelayPi")

package com.homemods.relay.pi

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