package com.example.cardapiodigital.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardapiodigital.data.database.AppDatabase
import com.example.cardapiodigital.data.entity.BebidaEntity
import com.example.cardapiodigital.data.entity.CardapioEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class CardapioViewModel(
    application: Application
) : AndroidViewModel(application) {

    /*
     * ======================================================
     * BANCO
     * ======================================================
     */

    private val database =
        AppDatabase.getDatabase(application)


    /*
     * DAOs
     */

    private val cardapioDao =
        database.cardapioDao()

    private val bebidaDao =
        database.bebidaDao()

    private val cardapioBebidaDao =
        database.cardapioBebidaDao()


    /*
     * ======================================================
     * CARDÁPIO PÚBLICO
     * ======================================================
     */

    private val _bebidasCardapioAtivo =
        MutableStateFlow<List<BebidaEntity>>(
            emptyList()
        )

    val bebidasCardapioAtivo =
        _bebidasCardapioAtivo.asStateFlow()


    /*
     * ======================================================
     * TODOS OS CARDÁPIOS
     * ======================================================
     */

    val cardapios =
        cardapioDao
            .listarTodos()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )


    /*
     * ======================================================
     * CARDÁPIO ATIVO
     * ======================================================
     */

    val cardapioAtivo =
        cardapioDao
            .observarCardapioAtivo()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )


    /*
     * ======================================================
     * TODAS AS BEBIDAS CADASTRADAS
     * ======================================================
     */

    val bebidasDisponiveis =
        bebidaDao
            .listarTodas()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )


    /*
     * ======================================================
     * BEBIDAS SELECIONADAS
     * ======================================================
     */

    private val _bebidasSelecionadas =
        MutableStateFlow<Set<Long>>(
            emptySet()
        )

    val bebidasSelecionadas =
        _bebidasSelecionadas.asStateFlow()


    /*
     * ======================================================
     * INICIALIZAÇÃO
     * ======================================================
     */

    init {

        garantirCardapioFixo()

        observarBebidasCardapioAtivo()
    }


    /*
     * ======================================================
     * GARANTIR CARDÁPIO FIXO
     * ======================================================
     */

    private fun garantirCardapioFixo() {

        viewModelScope.launch {

            val cardapioFixo =
                cardapioDao
                    .buscarCardapioFixo()


            if (cardapioFixo == null) {

                cardapioDao.inserir(

                    CardapioEntity(
                        nome = "Cardápio Fixo",
                        descricao = "Cardápio principal",
                        fixo = true,
                        ativo = true
                    )
                )
            }
        }
    }


    /*
     * ======================================================
     * CRIAR CARDÁPIO
     * ======================================================
     */

    fun criarCardapio(
        nome: String,
        descricao: String
    ) {

        val nomeLimpo =
            nome.trim()

        val descricaoLimpa =
            descricao.trim()


        if (nomeLimpo.isBlank()) {
            return
        }


        viewModelScope.launch {

            cardapioDao.inserir(

                CardapioEntity(
                    nome = nomeLimpo,
                    descricao = descricaoLimpa,
                    fixo = false,
                    ativo = false
                )
            )
        }
    }


    /*
     * ======================================================
     * EDITAR CARDÁPIO
     * ======================================================
     */

    fun editarCardapio(
        cardapio: CardapioEntity,
        nome: String,
        descricao: String
    ) {

        val nomeLimpo =
            nome.trim()

        val descricaoLimpa =
            descricao.trim()


        if (nomeLimpo.isBlank()) {
            return
        }


        viewModelScope.launch {

            cardapioDao.atualizar(

                cardapio.copy(
                    nome = nomeLimpo,
                    descricao = descricaoLimpa
                )
            )
        }
    }


    /*
     * ======================================================
     * ATIVAR CARDÁPIO
     * ======================================================
     */

    fun ativarCardapio(
        cardapioId: Long
    ) {

        viewModelScope.launch {

            cardapioDao
                .ativarCardapio(
                    cardapioId
                )
        }
    }


    /*
     * ======================================================
     * EXCLUIR CARDÁPIO
     * ======================================================
     *
     * O cardápio fixo nunca pode ser excluído.
     *
     * Se o cardápio excluído estiver ativo,
     * ativamos automaticamente o cardápio fixo.
     */

    fun excluirCardapio(
        cardapio: CardapioEntity
    ) {

        if (cardapio.fixo) {
            return
        }


        viewModelScope.launch {

            val estavaAtivo =
                cardapio.ativo


            /*
             * Exclui somente cardápios
             * personalizados.
             */

            cardapioDao
                .excluirPersonalizado(
                    cardapio.id
                )


            /*
             * Se o cardápio excluído
             * era justamente o ativo,
             * voltamos para o fixo.
             */

            if (estavaAtivo) {

                val cardapioFixo =
                    cardapioDao
                        .buscarCardapioFixo()


                if (cardapioFixo != null) {

                    cardapioDao
                        .ativarCardapio(
                            cardapioFixo.id
                        )
                }
            }
        }
    }


    /*
     * ======================================================
     * BEBIDAS DO CARDÁPIO
     * ======================================================
     */


    /*
     * ======================================================
     * CARREGAR BEBIDAS DO CARDÁPIO
     * ======================================================
     */

    fun carregarBebidasDoCardapio(
        cardapioId: Long
    ) {

        /*
         * Primeiro limpamos a seleção
         * do cardápio anterior.
         */

        _bebidasSelecionadas.value =
            emptySet()


        viewModelScope.launch {

            val ids =
                cardapioBebidaDao
                    .listarIdsBebidas(
                        cardapioId
                    )


            _bebidasSelecionadas.value =
                ids.toSet()
        }
    }


    /*
     * ======================================================
     * MARCAR / DESMARCAR BEBIDA
     * ======================================================
     */

    fun alterarSelecaoBebida(
        bebidaId: Long,
        selecionada: Boolean
    ) {

        val novaSelecao =
            _bebidasSelecionadas
                .value
                .toMutableSet()


        if (selecionada) {

            novaSelecao.add(
                bebidaId
            )

        } else {

            novaSelecao.remove(
                bebidaId
            )
        }


        _bebidasSelecionadas.value =
            novaSelecao
    }


    /*
     * ======================================================
     * SALVAR BEBIDAS DO CARDÁPIO
     * ======================================================
     */

    fun salvarBebidasDoCardapio(
        cardapioId: Long,
        aoConcluir: () -> Unit
    ) {

        viewModelScope.launch {

            /*
             * Mantemos a ordem em que
             * as bebidas aparecem no catálogo.
             */

            val idsOrdenados =
                bebidasDisponiveis
                    .value
                    .filter { bebida ->

                        _bebidasSelecionadas
                            .value
                            .contains(
                                bebida.id
                            )
                    }
                    .map { bebida ->

                        bebida.id
                    }


            cardapioBebidaDao
                .substituirBebidasDoCardapio(

                    cardapioId =
                        cardapioId,

                    bebidaIds =
                        idsOrdenados
                )


            aoConcluir()
        }
    }


    /*
     * ======================================================
     * LIMPAR SELEÇÃO
     * ======================================================
     */

    fun limparSelecaoBebidas() {

        _bebidasSelecionadas.value =
            emptySet()
    }


    /*
     * ======================================================
     * OBSERVAR BEBIDAS DO CARDÁPIO ATIVO
     * ======================================================
     *
     * Sempre que o cardápio ativo mudar,
     * passamos a observar as bebidas
     * pertencentes ao novo cardápio.
     */

    private fun observarBebidasCardapioAtivo() {

        viewModelScope.launch {

            cardapioDao
                .observarCardapioAtivo()
                .collectLatest { cardapio ->

                    /*
                     * Nenhum cardápio ativo.
                     */

                    if (cardapio == null) {

                        _bebidasCardapioAtivo.value =
                            emptyList()

                        return@collectLatest
                    }


                    /*
                     * Observa as bebidas
                     * do cardápio ativo.
                     */

                    cardapioBebidaDao
                        .listarBebidasDoCardapio(
                            cardapio.id
                        )
                        .collect { bebidas ->

                            _bebidasCardapioAtivo.value =
                                bebidas
                        }
                }
        }
    }
}