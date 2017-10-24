package com.homemods.relay.bluetooth

import java.io.InputStream
import java.io.OutputStream

/**
 * @author sergeys
 */
interface BluetoothConnection {
    fun openInputStream(): InputStream
    fun openOutputStream(): OutputStream
    
    fun close()
}