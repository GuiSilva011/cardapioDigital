package com.example.cardapiodigital.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardapiodigital.data.database.AppDatabase
import com.example.cardapiodigital.data.entity.BebidaEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import android.net.Uri
import com.example.cardapiodigital.data.storage.ImagemStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BebidaViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val bebidaDao =
        AppDatabase
            .getDatabase(application)
            .bebidaDao()

    val bebidas = bebidaDao
        .listarTodas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun cadastrarBebida(
        nome: String,
        descricao: String,
        imagemUri: Uri?
    ) {

        val nomeLimpo = nome.trim()
        val descricaoLimpa = descricao.trim()

        if (nomeLimpo.isBlank()) {
            return
        }

        viewModelScope.launch {

            val imagemPath =
                withContext(Dispatchers.IO) {

                    imagemUri?.let { uri ->

                        ImagemStorage.salvarImagem(
                            context = getApplication(),
                            uri = uri
                        )
                    }
                }

            val bebida = BebidaEntity(
                nome = nomeLimpo,
                descricao = descricaoLimpa,
                imagemPath = imagemPath
            )

            bebidaDao.inserir(bebida)
        }
    }

    fun editarBebida(
        bebida: BebidaEntity,
        nome: String,
        descricao: String,
        novaImagemUri: Uri?
    ) {

        val nomeLimpo = nome.trim()
        val descricaoLimpa = descricao.trim()

        if (nomeLimpo.isBlank()) {
            return
        }

        viewModelScope.launch {

            var imagemPath =
                bebida.imagemPath

            if (novaImagemUri != null) {

                val novaImagemPath =
                    withContext(Dispatchers.IO) {

                        ImagemStorage.salvarImagem(
                            context = getApplication(),
                            uri = novaImagemUri
                        )
                    }

                if (novaImagemPath != null) {

                    val imagemAntiga =
                        bebida.imagemPath

                    imagemPath =
                        novaImagemPath

                    bebidaDao.atualizar(
                        bebida.copy(
                            nome = nomeLimpo,
                            descricao = descricaoLimpa,
                            imagemPath = imagemPath
                        )
                    )

                    withContext(Dispatchers.IO) {
                        ImagemStorage.excluirImagem(
                            imagemAntiga
                        )
                    }

                    return@launch
                }
            }

            bebidaDao.atualizar(
                bebida.copy(
                    nome = nomeLimpo,
                    descricao = descricaoLimpa,
                    imagemPath = imagemPath
                )
            )
        }
    }

    fun excluirBebida(
        bebida: BebidaEntity
    ) {

        viewModelScope.launch {

            bebidaDao.excluir(bebida)

            withContext(Dispatchers.IO) {

                ImagemStorage.excluirImagem(
                    bebida.imagemPath
                )
            }
        }
    }

    fun alterarStatus(
        bebida: BebidaEntity,
        ativo: Boolean
    ) {
        viewModelScope.launch {

            bebidaDao.alterarStatus(
                id = bebida.id,
                ativo = ativo
            )
        }
    }
}