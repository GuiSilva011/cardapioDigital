package com.example.cardapiodigital.data.security

import android.content.Context
import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class AdminPinStore(context: Context) {

    private val preferences =
        context.getSharedPreferences(
            PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )

    fun possuiPin(): Boolean =
        preferences.contains(KEY_HASH) &&
                preferences.contains(KEY_SALT) &&
                preferences.contains(KEY_ALGORITHM)

    fun salvarPin(pin: String) {
        require(pinValido(pin)) {
            "O PIN deve conter exatamente $PIN_LENGTH números."
        }

        val salt =
            ByteArray(SALT_SIZE_BYTES).also {
                SecureRandom().nextBytes(it)
            }

        val algorithm =
            escolherAlgoritmo()

        val hash =
            gerarHash(
                pin = pin,
                salt = salt,
                algorithm = algorithm
            )

        preferences
            .edit()
            .putString(
                KEY_HASH,
                Base64.encodeToString(
                    hash,
                    Base64.NO_WRAP
                )
            )
            .putString(
                KEY_SALT,
                Base64.encodeToString(
                    salt,
                    Base64.NO_WRAP
                )
            )
            .putString(
                KEY_ALGORITHM,
                algorithm
            )
            .apply()
    }

    fun validarPin(pin: String): Boolean {
        if (!pinValido(pin)) {
            return false
        }

        val hashSalvo =
            preferences.getString(
                KEY_HASH,
                null
            ) ?: return false

        val saltSalvo =
            preferences.getString(
                KEY_SALT,
                null
            ) ?: return false

        val algorithm =
            preferences.getString(
                KEY_ALGORITHM,
                null
            ) ?: return false

        return try {
            val hashEsperado =
                Base64.decode(
                    hashSalvo,
                    Base64.NO_WRAP
                )

            val salt =
                Base64.decode(
                    saltSalvo,
                    Base64.NO_WRAP
                )

            val hashInformado =
                gerarHash(
                    pin = pin,
                    salt = salt,
                    algorithm = algorithm
                )

            MessageDigest.isEqual(
                hashEsperado,
                hashInformado
            )
        } catch (_: Exception) {
            false
        }
    }

    private fun escolherAlgoritmo(): String =
        try {
            SecretKeyFactory.getInstance(
                ALGORITHM_SHA256
            )

            ALGORITHM_SHA256
        } catch (_: Exception) {
            ALGORITHM_SHA1
        }

    private fun gerarHash(
        pin: String,
        salt: ByteArray,
        algorithm: String
    ): ByteArray {
        val keySpec =
            PBEKeySpec(
                pin.toCharArray(),
                salt,
                HASH_ITERATIONS,
                HASH_SIZE_BITS
            )

        return try {
            SecretKeyFactory
                .getInstance(algorithm)
                .generateSecret(keySpec)
                .encoded
        } finally {
            keySpec.clearPassword()
        }
    }

    companion object {
        const val PIN_LENGTH = 4

        private const val PREFERENCES_NAME =
            "admin_security"

        private const val KEY_HASH =
            "pin_hash"

        private const val KEY_SALT =
            "pin_salt"

        private const val KEY_ALGORITHM =
            "pin_algorithm"

        private const val SALT_SIZE_BYTES = 16
        private const val HASH_ITERATIONS = 120_000
        private const val HASH_SIZE_BITS = 256

        private const val ALGORITHM_SHA256 =
            "PBKDF2WithHmacSHA256"

        private const val ALGORITHM_SHA1 =
            "PBKDF2WithHmacSHA1"

        fun pinValido(pin: String): Boolean =
            pin.length == PIN_LENGTH &&
                    pin.all { caractere ->
                        caractere.isDigit()
                    }
    }
}
