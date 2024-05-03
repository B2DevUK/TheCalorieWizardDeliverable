package com.example.thecaloriewizard.encryption

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * [EncryptionManager]
 * Description: Singleton object for managing encryption and decryption operations.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Parameters/Arguments]
 * None
 *
 * [Return Value]
 * None
 *
 * [Usage]
 * This object provides methods to initialize encryption, encrypt data, and decrypt data.
 *
 * [Notes]
 * This implementation uses AES encryption with GCM mode.
 */
object EncryptionManager {

    private const val KEYSTORE = "AndroidKeyStore"
    private const val KEY_ALIAS = "myKeyAlias"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val TAG_LENGTH_BIT = 128
    private const val IV_SIZE = 12

    /**
     * [initialize]
     * Description: Initializes the encryption by generating a secret key if it doesn't exist.
     */
    fun initialize(context: Context) {
        generateKey()
    }

    private fun generateKey() {
        val keyStore = java.security.KeyStore.getInstance(KEYSTORE)
        keyStore.load(null)

        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE)
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setRandomizedEncryptionRequired(true)
                .build()

            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        }
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = java.security.KeyStore.getInstance(KEYSTORE)
        keyStore.load(null)
        return keyStore.getKey(KEY_ALIAS, null) as SecretKey
    }

    /**
     * [encrypt]
     * Description: Encrypts the given data.
     *
     * [Return Value]
     * The encrypted data as a byte array.
     */
    fun encrypt(data: String): ByteArray {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        val iv = cipher.iv
        val encrypted = cipher.doFinal(data.toByteArray(Charsets.UTF_8))

        return iv + encrypted
    }

    /**
     * [decrypt]
     * Description: Decrypts the given data.
     *
     * [Return Value]
     * The decrypted data as a string.
     */
    fun decrypt(data: ByteArray): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val iv = data.copyOfRange(0, IV_SIZE)
        val encryptedData = data.copyOfRange(IV_SIZE, data.size)

        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), GCMParameterSpec(TAG_LENGTH_BIT, iv))
        val decrypted = cipher.doFinal(encryptedData)

        return String(decrypted, Charsets.UTF_8)
    }
}