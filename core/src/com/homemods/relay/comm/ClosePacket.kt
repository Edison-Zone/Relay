package com.homemods.relay.comm

/**
 * This packet represents the final packet in a connection.
 * It should be sent before the connection is closed by the party closing the connection.
 *
 * This packet is optional but recommended for a clean shutdown of the connection.
 *
 * @author sergeys
 *
 * @constructor Creates a new ClosePacket
 */
class ClosePacket : Packet()