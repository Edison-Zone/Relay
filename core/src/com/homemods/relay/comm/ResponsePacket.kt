package com.homemods.relay.comm

import java.io.Serializable

/**
 * This packet represents a response to another packet.
 * It references the other packet by its id, so when expecting a response the id of the packet should be noted.
 *
 * The type of the response should be determined by the original type of packet sent.
 *
 * @author sergeys
 *
 * @constructor Creates a new ResponsePacket
 * @property sourceId - the id of the packet which this is the response to
 * @property response - an object representing the response
 */
class ResponsePacket(val sourceId: Long, val response: Serializable) : Packet()