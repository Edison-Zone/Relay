@file:JvmName("Bluetooth")

package com.homemods.bluetooth

import java.nio.file.Files

/**
 * @author sergeys
 */
fun loadNativesIfNeeded() {
    val library = BluetoothServerNativeImpl::class.java.getResourceAsStream("libEdisonNative.so")
    
    val tempFile = Files.createTempFile("libEdisonNative", "")
    
    val outputStream = Files.newOutputStream(tempFile)
    
    library.copyTo(outputStream)
    
    outputStream.close()
    library.close()
    
    System.loadLibrary(tempFile.toAbsolutePath().toString())
}
