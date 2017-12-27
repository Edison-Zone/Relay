package com.homemods.relay.connection

import java.io.InputStream
import java.io.OutputStream

/**
 * @author sergeys
 */
interface ClientConnection {
    fun openInputStream(): InputStream
    fun openOutputStream(): OutputStream
    
    fun close()
}