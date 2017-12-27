package com.homemods.relay.pi.bluetooth

/*import javax.microedition.io.StreamConnection

/**
 * @author sergeys
 */
class PiBluetoothConnection(private val actual: StreamConnection) : ClientConnection {
    override fun openInputStream(): InputStream = actual.openInputStream()
    
    override fun openOutputStream(): OutputStream = actual.openDataOutputStream()
    
    override fun close() = actual.close()
}*/