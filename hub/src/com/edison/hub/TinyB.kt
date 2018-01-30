//package com.edison.hub
//
//import tinyb.BluetoothException
//import tinyb.BluetoothManager
//
///**
// * @author sergeys
// */
//
//fun main(args: Array<String>) {
//    /*
//     * To start looking of the device, we first must initialize the TinyB library. The way of interacting with the
//     * library is through the BluetoothManager. There can be only one BluetoothManager at one time, and the
//     * reference to it is obtained through the getBluetoothManager method.
//     */
//    val manager = BluetoothManager.getBluetoothManager()
//
//    /*
//     * The manager will try to initialize a BluetoothAdapter if any adapter is present in the system. To initialize
//     * discovery we can call startDiscovery, which will put the default adapter in discovery mode.
//     */
//    val discoveryStarted = manager.startDiscovery()
//
//    if (!discoveryStarted) {
//        System.err.println("Failed to start discovery")
//        System.exit(-1)
//    }
//
//    Thread.sleep(5000) // wait 5 seconds
//
//    try {
//        manager.stopDiscovery()
//    } catch (e: BluetoothException) {
//        System.err.println("Discovery could not be stopped.")
//    }
//
//    val device = manager.devices.firstOrNull { d -> if (USE_NAME) d.name == deviceName else d.address == deviceAddress }
//
//    if (device == null) {
//        System.err.println("Failed to find device")
//        System.exit(-1)
//        return
//    }
//
//    if (device.connect())
//        println("Connected")
//    else {
//        println("Could not connect")
//        System.exit(-1)
//        return
//    }
//
//    print("Resolving services")
//    while (!device.servicesResolved) {
//        Thread.sleep(100)
//        print(".")
//    }
//    println()
//
//    val service = device.services.firstOrNull { s -> s.uuid == serviceUUID.toLowerCase() }
//
//    if (service == null) {
//        System.err.println("Failed to find service")
//        System.exit(-1)
//        return
//    }
//
//    val dataSendChar = service.characteristics.firstOrNull { c -> c.uuid == dataSendUUID.toLowerCase() }
//    val dataReadChar = service.characteristics.firstOrNull { c -> c.uuid == dataReadUUID.toLowerCase() }
//
//    if (dataSendChar == null || dataReadChar == null) {
//        System.err.println("Failed to find characteristics")
//        System.exit(1)
//        return
//
//    }
//
//    val size = 10
//    val times = 100
//
//    var lastInt = 0
//    var count = 0
//    val sent = ByteArray(size)
//
//    val doSend = fun() {
//        if (count < times) {
//            for (j in 0 until size) {
//                sent[j] = (lastInt * lastInt++).toByte()
//            }
//
//            dataSendChar.writeValue(sent)
//            count++
//        }
//    }
//
//
//    dataReadChar.enableValueNotifications { bytes ->
//        if (bytes != null) {
//            val success = sent.mapIndexed { index, byte -> bytes[index] == (byte + 1).toByte() }.all { b -> b == true }
//
//            if (success) {
//                println("Success")
//            } else {
//                println("Failed")
//            }
//
//            doSend()
//        }
//    }
//
//    doSend()
//
//    dataReadChar.disableValueNotifications()
//
//    device.disconnect()
//}
