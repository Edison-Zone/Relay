@file:JvmName("Main")

package com.homemods.spoke

import com.homemods.bluetooth.BluetoothClient
import com.homemods.message.decrypted
import com.homemods.message.getMessageData
import com.homemods.message.getMessageID
import com.homemods.spoke.pin.PiPinFactory

/**
 * @author sergeys
 */

var millis = 1.0

fun main(args: Array<String>) {
    val bluetoothClient = BluetoothClient.native()
    
    val bluetoothClientSocket = bluetoothClient.startClient()
    
    //Ports are odd numbers in the range from 0x1001 to 0x8FFF
    
    //Wait until connected to the hub
    val bluetoothSocket = bluetoothClientSocket.connect("B8:27:EB:CB:E5:24", 0x1001)
    
    val stream = bluetoothSocket.createStream()
    
    val pinFactory = PiPinFactory()
    val pwmPin = pinFactory.createOutputPin(7)
    
    Thread {
        while (true) {
            val int = millis.toLong()
            val nanos = ((millis - int) * 1000000).toInt()
            
            pwmPin.on()
            Thread.sleep(int, nanos)
            pwmPin.off()
            
            val remainder = 50 - millis
            val rint = remainder.toLong()
            val rnanos = ((remainder - rint) * 1000000).toInt()
            
            Thread.sleep(rint, rnanos)
        }
    }.apply {
        name = "PWM Thread"
        isDaemon = true
        start()
    }
    
    repeat(15) {
        val bytes = stream.read().decrypted()
        val messageID = getMessageID(bytes)
        println("Recieved message $messageID")
        
        val data = getMessageData(bytes)
        if (data[0].toInt() and 0xFF == 0b0000) { //If it should make the next byte the state of the motor
            val state = data[1].toInt() and 0xFF //Get rid of the sign extension
            //Expecting a value 0 to 1024
            //This means a duty cycle of 2.5% is 25.6
            //A duty cycle of 12.5% is 128
            if (state > 0) {
                millis = 2.0
            } else {
                millis = 1.0
            }
            //pwmPin.setPwm(((state/25.5 + 2.5)/100*1024).roundToInt())
        }
    }
    
    bluetoothSocket.close()
}