package com.homemods.relay.comm

/**
 * A packet for setting the state of the server.
 *
 * @author sergeys
 *
 * @constructor Creates a new SetStatePacket
 * @property state - the state that you are setting for the server
 */
class SetStatePacket(val state: Boolean) : Packet()