package com.gaurav.ainotificationassistant.data.local.db

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import java.io.File
import java.security.SecureRandom
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec

class DatabaseKeyManager(private val context: Context) {

    private val keyStoreAlias = "aiassistant_db_key"
    private val keyFileName = "db_key.dat"
    private val keyDirectory: File
        get() = File(context.filesDir, ".keys").apply { mkdirs() }

    fun getOrCreatePassphrase(): ByteArray {
        val keyFile = File(keyDirectory, keyFileName)

        return if (keyFile.exists()) {
            decryptPassphrase(keyFile)
        } else {
            val passphrase = generatePassphrase()
            encryptAndStorePassphrase(passphrase, keyFile)
            passphrase
        }
    }

    private fun generatePassphrase(): ByteArray {
        val random = SecureRandom.getInstanceStrong()
        val passphrase = ByteArray(32)
        random.nextBytes(passphrase)
        return passphrase
    }

    private fun encryptAndStorePassphrase(passphrase: ByteArray, keyFile: File) {
        try {
            val masterKey = getMasterKey()
            val encryptedFile = EncryptedFile.Builder(
                context,
                keyFile,
                masterKey,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()

            encryptedFile.openFileOutput().use { output ->
                output.write(passphrase)
                output.flush()
            }
        } catch (e: Exception) {
            throw RuntimeException("Failed to encrypt passphrase", e)
        }
    }

    private fun decryptPassphrase(keyFile: File): ByteArray {
        return try {
            val masterKey = getMasterKey()
            val encryptedFile = EncryptedFile.Builder(
                context,
                keyFile,
                masterKey,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()

            encryptedFile.openFileInput().use { input ->
                input.readBytes()
            }
        } catch (e: Exception) {
            throw RuntimeException("Failed to decrypt passphrase", e)
        }
    }

    private fun getMasterKey(): MasterKey {
        return MasterKey.Builder(context, keyStoreAlias)
            .setKeyGenParameterSpec(
                KeyGenParameterSpec.Builder(
                    keyStoreAlias,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build()
            )
            .build()
    }
}
