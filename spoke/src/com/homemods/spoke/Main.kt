@file:JvmName("Main")

package com.homemods.spoke

import com.homemods.bluetooth.BluetoothClient
import com.homemods.message.decrypted
import com.homemods.message.getMessageData
import com.homemods.message.getMessageID
import com.homemods.spoke.pin.PiPinFactory
import kotlin.math.roundToLong

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
            val nanos = ((millis) * 1000000).roundToLong()
            val endTime = System.nanoTime() + nanos
            
            pwmPin.on()
            while (System.nanoTime() < endTime);
            pwmPin.off()
    
            val restEnd = 50_000_000 - nanos + endTime
    
            while (System.nanoTime() < restEnd);
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
    
            millis = 0.5 + 2 * state / 255.0
        }
    }
    
    bluetoothSocket.close()
}