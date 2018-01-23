@file:JvmName("Bluetooth")

package com.homemods.bluetooth

/**
 * @author sergeys
 */
fun loadNativesIfNeeded() {
    System.loadLibrary("EdisonNative")
    //    val library = BluetoothServerNativeImpl::class.java.getResourceAsStream("libEdisonNative.so")
    //
    //    val tempFile = Files.createTempFile("libEdisonNative", "")
    //
    //    val outputStream = Files.newOutputStream(tempFile)
    //
    //    library.copyTo(outputStream)
    //
    //    outputStream.close()
    //    library.close()
    //
    //    System.loadLibrary(tempFile.toAbsolutePath().toString())
}
