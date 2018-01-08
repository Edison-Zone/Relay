package com.homemods.relay.comm

import java.io.Serializable
import java.util.Random

/**
 * Represents a single unit of transferrable data
 *
 * It is marked by an random id to be used as a reference to this packet if needed
 *
 * @author sergeys
 */
abstract class Packet : Serializable {
    val id = rand.nextLong()
}

val rand = Random()