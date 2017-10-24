package com.homemods.relay.pi.bluetooth

import com.homemods.relay.bluetooth.BluetoothConnection
import java.io.InputStream
import java.io.OutputStream
import javax.microedition.io.StreamConnection

/**
 * @author sergeys
 */
class PiBluetoothConnection(private val actual: StreamConnection) : BluetoothConnection {
    override fun openInputStream(): InputStream = actual.openInputStream()
    
    override fun openOutputStream(): OutputStream = actual.openDataOutputStream()
    
    override fun close() = actual.close()
}