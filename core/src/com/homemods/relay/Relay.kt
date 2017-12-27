package com.homemods.relay

import com.homemods.relay.connection.ClientConnection
import com.homemods.relay.connection.ConnectionFactory
import com.homemods.relay.pin.PinFactory
import java.util.Random

/**
 * @author sergeys
 */

class Relay(val pinFactory: PinFactory, val connectionFactory: ConnectionFactory) {
    fun run() {
        val connection = connectionFactory.run {
            var conn: ClientConnection? = null
            
            beginDiscovery { connection ->
                conn = connection
            }
            while (conn == null) {
                Thread.sleep(1000)
            }
        
            endDiscovery()
            conn!!
        }
    
        val outputStream = connection.openOutputStream()
    
        //val pins = Array<OutputPin>(8) { pinNum -> pinFactory.createOutputPin(pinNum) }
        //pins.forEach { pin ->
        //    pin.setShutdownState(PinState.OFF)
        //}
        
        val rand = Random()
        //val bytes = ByteArray(1)
        //var num: Int
    
        for (i in 1..60) {
            //rand.nextBytes(bytes)
            //num = bytes[0].toInt()
        
            //Send over our random byte
            outputStream.write(rand.nextInt(Int.MAX_VALUE))
            outputStream.flush()
        
            /*
            for (i in 0..7) {
                num = num ushr 1
                pins[i].set(if (num and 0b0000_0001 != 0) {
                    PinState.ON
                } else {
                    PinState.OFF
                })
            }
            */
            Thread.sleep(1000)
        }
    
        outputStream.close()
        connection.close()
    }
}

