package com.edison.message

import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


/**
 * @author sergeys
 */

private val random = SecureRandom()

//Random cryptographic key
private val key: SecretKey = run {
    val decodedKey = Base64.getDecoder().decode("dXGsemvjL8gXtLgXY6fu/g==")
    SecretKeySpec(decodedKey, 0, decodedKey.size, "AES")
}

//Iv bytes (also random)
private val ivBytes = ByteArray(16)

fun formatMessage(messageId: Int, data: ByteArray): ByteArray {
    val bytes = ByteArray(4 + data.size)
    
    formatMessage(bytes, messageId, data)
    
    return bytes
}

fun formatMessage(location: ByteArray, messageId: Int, data: ByteArray): Int {
    if (location.size < 4 + data.size) {
        return -1
    }
    
    location[0] = ((messageId and 0xFF0000) ushr 16).toByte()
    location[1] = ((messageId and 0x00FF00) ushr 8).toByte()
    location[2] = (messageId and 0x0000FF).toByte()
    location[3] = random.nextInt().toByte()
    System.arraycopy(data, 0, location, 4, data.size)
    
    return 4 + data.size
}

fun getMessageID(messageBytes: ByteArray): Int {
    return ((messageBytes[0].toInt() and 0xFF) shl 16) or ((messageBytes[1].toInt() and 0xFF) shl 8) or
            ((messageBytes[2].toInt() and 0xFF))
}

fun getMessageData(messageBytes: ByteArray): ByteArray {
    val bytes = ByteArray(messageBytes.size - 4)
    
    System.arraycopy(messageBytes, 4, bytes, 0, bytes.size)
    
    return bytes
}

fun copyMessageData(location: ByteArray, messageBytes: ByteArray): Int {
    if (location.size < messageBytes.size - 4) {
        return -1
    }
    
    System.arraycopy(messageBytes, 4, location, 0, messageBytes.size - 4)
    
    return messageBytes.size - 4
}

fun encrypt(bytes: ByteArray): ByteArray {
    val encCipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    encCipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(ivBytes))
    
    return encCipher.doFinal(bytes)
}

fun ByteArray.encrypted() = encrypt(this)

fun decrypt(bytes: ByteArray): ByteArray {
    val decCipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    decCipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(ivBytes))
    
    return decCipher.doFinal(bytes)
}

fun ByteArray.decrypted() = decrypt(this)