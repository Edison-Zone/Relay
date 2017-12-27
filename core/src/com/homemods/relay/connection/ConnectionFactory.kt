package com.homemods.relay.connection

/**
 * @author sergeys
 */
interface ConnectionFactory {
    fun beginDiscovery(onDiscovery: (ClientConnection) -> Unit)
    fun endDiscovery()
}
