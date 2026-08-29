package com.example.cardapiodigital.data.storage

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import java.io.File
import java.util.UUID

object ImagemStorage {

    fun salvarImagem(
        context: Context,
        uri: Uri
    ): String? {

        return try {

            val pasta = File(
                context.filesDir,
                "bebidas"
            )

            if (!pasta.exists()) {
                pasta.mkdirs()
            }

            val mimeType =
                context.contentResolver.getType(uri)

            val extensao =
                MimeTypeMap
                    .getSingleton()
                    .getExtensionFromMimeType(mimeType)
                    ?: "jpg"

            val nomeArquivo =
                "${UUID.randomUUID()}.$extensao"

            val arquivoDestino =
                File(
                    pasta,
                    nomeArquivo
                )

            context.contentResolver
                .openInputStream(uri)
                ?.use { input ->

                    arquivoDestino
                        .outputStream()
                        .use { output ->

                            input.copyTo(output)
                        }
                }

            if (arquivoDestino.exists()) {
                arquivoDestino.absolutePath
            } else {
                null
            }

        } catch (e: Exception) {

            e.printStackTrace()

            null
        }
    }

    fun excluirImagem(
        caminho: String?
    ) {

        if (caminho.isNullOrBlank()) {
            return
        }

        try {

            val arquivo =
                File(caminho)

            if (arquivo.exists()) {
                arquivo.delete()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}