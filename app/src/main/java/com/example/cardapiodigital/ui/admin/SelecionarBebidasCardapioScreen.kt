package com.example.cardapiodigital.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.cardapiodigital.data.entity.BebidaEntity
import com.example.cardapiodigital.viewmodel.CardapioViewModel
import java.io.File


/*
 * =========================================================
 * PALETA ADMIN
 * =========================================================
 */

private val AdminBackground =
    Color(0xFF050505)

private val AdminSurface =
    Color(0xFF111111)

private val AdminBorder =
    Color(0xFF3A3123)

private val AdminText =
    Color(0xFFC6A15B)

private val AdminTextSecondary =
    Color(0xFFFFFFFF)

private val AdminDark =
    Color(0xFFC6A15B)

private val AdminGold =
    Color(0xFFC6A15B)

private val AdminSoft =
    Color(0xFF191919)

private val AdminGreen =
    Color(0xFF3E7652)

private val AdminDisabled =
    Color(0xFF8A8A8A)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelecionarBebidasCardapioScreen(
    cardapioId: Long,
    nomeCardapio: String,
    viewModel: CardapioViewModel,
    onVoltar: () -> Unit
) {

    val bebidas by
    viewModel
        .bebidasDisponiveis
        .collectAsStateWithLifecycle()

    val bebidasSelecionadas by
    viewModel
        .bebidasSelecionadas
        .collectAsStateWithLifecycle()


    var busca by remember {
        mutableStateOf("")
    }


    var mostrarSomenteSelecionadas by remember {
        mutableStateOf(false)
    }


    /*
     * Carrega as bebidas que já pertencem
     * ao cardápio quando abrimos a tela.
     */
    LaunchedEffect(cardapioId) {

        viewModel
            .carregarBebidasDoCardapio(
                cardapioId
            )
    }


    /*
     * Busca + filtro de selecionadas.
     */
    val bebidasFiltradas =
        bebidas.filter { bebida ->

            val correspondeBusca =
                busca.isBlank() ||
                        bebida.nome.contains(
                            busca,
                            ignoreCase = true
                        ) ||
                        bebida.descricao.contains(
                            busca,
                            ignoreCase = true
                        )


            val correspondeSelecao =
                !mostrarSomenteSelecionadas ||
                        bebidasSelecionadas.contains(
                            bebida.id
                        )


            correspondeBusca &&
                    correspondeSelecao
        }


    val quantidadeSelecionadas =
        bebidasSelecionadas.size


    val quantidadeAtivas =
        bebidas.count {
            it.ativo
        }


    Scaffold(
        containerColor =
            AdminBackground,

        topBar = {

            TopAppBar(
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor =
                            AdminBackground
                    ),

                navigationIcon = {

                    TextButton(
                        onClick = {

                            viewModel
                                .limparSelecaoBebidas()

                            onVoltar()
                        }
                    ) {

                        Text(
                            text = "← Voltar",

                            color =
                                AdminText
                        )
                    }
                },

                title = {

                    Text(
                        text =
                            "Bebidas do cardápio",

                        fontWeight =
                            FontWeight.SemiBold,

                        color =
                            AdminText
                    )
                }
            )
        },

        /*
         * =================================================
         * BARRA INFERIOR
         * =================================================
         */
        bottomBar = {

            BarraSalvarCardapio(
                quantidadeSelecionadas =
                    quantidadeSelecionadas,

                onCancelar = {

                    viewModel
                        .limparSelecaoBebidas()

                    onVoltar()
                },

                onSalvar = {

                    viewModel
                        .salvarBebidasDoCardapio(
                            cardapioId =
                                cardapioId,

                            aoConcluir = {

                                viewModel
                                    .limparSelecaoBebidas()

                                onVoltar()
                            }
                        )
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    paddingValues
                )
                .padding(
                    horizontal = 42.dp,
                    vertical = 20.dp
                )
        ) {

            /*
             * =================================================
             * CABEÇALHO
             * =================================================
             */

            Text(
                text =
                    "MONTAGEM DO CARDÁPIO",

                fontSize = 11.sp,

                letterSpacing =
                    1.6.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    AdminGold
            )


            Spacer(
                modifier =
                    Modifier.height(
                        7.dp
                    )
            )


            Text(
                text =
                    nomeCardapio,

                fontSize = 32.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    AdminText
            )


            Spacer(
                modifier =
                    Modifier.height(
                        6.dp
                    )
            )


            Text(
                text =
                    "Escolha quais bebidas serão exibidas quando este cardápio estiver ativo.",

                fontSize = 15.sp,

                color =
                    AdminTextSecondary
            )


            Spacer(
                modifier =
                    Modifier.height(
                        24.dp
                    )
            )


            /*
             * =================================================
             * RESUMO
             * =================================================
             */

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(
                        14.dp
                    )
            ) {

                ResumoSelecaoCard(
                    titulo =
                        "Disponíveis",

                    valor =
                        quantidadeAtivas.toString(),

                    modifier =
                        Modifier.weight(1f)
                )


                ResumoSelecaoCard(
                    titulo =
                        "Selecionadas",

                    valor =
                        quantidadeSelecionadas.toString(),

                    destacar =
                        quantidadeSelecionadas > 0,

                    modifier =
                        Modifier.weight(1f)
                )


                ResumoSelecaoCard(
                    titulo =
                        "Total no catálogo",

                    valor =
                        bebidas.size.toString(),

                    modifier =
                        Modifier.weight(1f)
                )
            }


            Spacer(
                modifier =
                    Modifier.height(
                        22.dp
                    )
            )


            /*
             * =================================================
             * BUSCA
             * =================================================
             */

            OutlinedTextField(
                value =
                    busca,

                onValueChange = {
                    busca = it
                },

                modifier =
                    Modifier.fillMaxWidth(),

                singleLine =
                    true,

                label = {

                    Text(
                        text =
                            "Buscar bebida"
                    )
                },

                placeholder = {

                    Text(
                        text =
                            "Digite o nome ou descrição..."
                    )
                },

                shape =
                    RoundedCornerShape(
                        12.dp
                    )
            )


            Spacer(
                modifier =
                    Modifier.height(
                        14.dp
                    )
            )


            /*
             * =================================================
             * FILTROS
             * =================================================
             */

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                BotaoFiltroSelecao(
                    texto =
                        "Todas",

                    selecionado =
                        !mostrarSomenteSelecionadas,

                    onClick = {

                        mostrarSomenteSelecionadas =
                            false
                    }
                )


                Spacer(
                    modifier =
                        Modifier.width(
                            8.dp
                        )
                )


                BotaoFiltroSelecao(
                    texto =
                        "Selecionadas ($quantidadeSelecionadas)",

                    selecionado =
                        mostrarSomenteSelecionadas,

                    onClick = {

                        mostrarSomenteSelecionadas =
                            true
                    }
                )


                Spacer(
                    modifier =
                        Modifier.weight(1f)
                )


                Text(
                    text =
                        "${bebidasFiltradas.size} bebida(s)",

                    fontSize = 12.sp,

                    color =
                        AdminTextSecondary
                )
            }


            Spacer(
                modifier =
                    Modifier.height(
                        18.dp
                    )
            )


            HorizontalDivider(
                color =
                    AdminBorder
            )


            Spacer(
                modifier =
                    Modifier.height(
                        16.dp
                    )
            )


            /*
             * =================================================
             * LISTA DE BEBIDAS
             * =================================================
             */

            when {

                bebidas.isEmpty() -> {

                    EstadoVazioSelecao(
                        titulo =
                            "Nenhuma bebida cadastrada",

                        descricao =
                            "Cadastre bebidas no catálogo antes de montar um cardápio."
                    )
                }


                bebidasFiltradas.isEmpty() -> {

                    EstadoVazioSelecao(
                        titulo =
                            if (
                                mostrarSomenteSelecionadas
                            ) {

                                "Nenhuma bebida selecionada"

                            } else {

                                "Nenhuma bebida encontrada"
                            },

                        descricao =
                            if (
                                mostrarSomenteSelecionadas
                            ) {

                                "Selecione bebidas na lista para adicioná-las a este cardápio."

                            } else {

                                "Tente pesquisar usando outro nome ou descrição."
                            }
                    )
                }


                else -> {

                    LazyColumn(
                        modifier =
                            Modifier.fillMaxSize(),

                        verticalArrangement =
                            Arrangement.spacedBy(
                                11.dp
                            )
                    ) {

                        items(
                            items =
                                bebidasFiltradas,

                            key = {
                                    bebida ->
                                bebida.id
                            }
                        ) { bebida ->

                            val selecionada =
                                bebidasSelecionadas
                                    .contains(
                                        bebida.id
                                    )


                            BebidaSelecaoCard(
                                bebida =
                                    bebida,

                                selecionada =
                                    selecionada,

                                onAlterarSelecao = {

                                    /*
                                     * Mantemos bebidas inativas
                                     * bloqueadas para inclusão.
                                     */
                                    if (bebida.ativo) {

                                        viewModel
                                            .alterarSelecaoBebida(
                                                bebidaId =
                                                    bebida.id,

                                                selecionada =
                                                    !selecionada
                                            )
                                    }
                                }
                            )
                        }


                        /*
                         * Espaço extra para a última bebida
                         * não ficar visualmente colada.
                         */
                        item {

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        20.dp
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun ResumoSelecaoCard(
    titulo: String,
    valor: String,
    modifier: Modifier = Modifier,
    destacar: Boolean = false
) {

    Card(
        modifier =
            modifier,

        shape =
            RoundedCornerShape(
                14.dp
            ),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (destacar) {

                        AdminGreen.copy(
                            alpha = 0.07f
                        )

                    } else {

                        AdminSurface
                    }
            ),

        border =
            BorderStroke(
                width =
                    1.dp,

                color =
                    if (destacar) {

                        AdminGreen.copy(
                            alpha = 0.35f
                        )

                    } else {

                        AdminBorder
                    }
            )
    ) {

        Column(
            modifier =
                Modifier.padding(
                    17.dp
                )
        ) {

            Text(
                text =
                    titulo.uppercase(),

                fontSize =
                    10.sp,

                letterSpacing =
                    0.8.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    if (destacar) {
                        AdminGreen
                    } else {
                        AdminTextSecondary
                    }
            )


            Spacer(
                modifier =
                    Modifier.height(
                        5.dp
                    )
            )


            Text(
                text =
                    valor,

                fontSize =
                    24.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    AdminText
            )
        }
    }
}


@Composable
private fun BotaoFiltroSelecao(
    texto: String,
    selecionado: Boolean,
    onClick: () -> Unit
) {

    if (selecionado) {

        Button(
            onClick =
                onClick,

            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        AdminDark,

                    contentColor =
                        AdminBackground
                ),

            shape =
                RoundedCornerShape(
                    10.dp
                )
        ) {

            Text(
                text =
                    texto,

                fontSize =
                    12.sp,

                fontWeight =
                    FontWeight.SemiBold
            )
        }

    } else {

        OutlinedButton(
            onClick =
                onClick,

            border =
                BorderStroke(
                    1.dp,
                    AdminBorder
                ),

            shape =
                RoundedCornerShape(
                    10.dp
                )
        ) {

            Text(
                text =
                    texto,

                fontSize =
                    12.sp,

                color =
                    AdminText
            )
        }
    }
}


@Composable
private fun BebidaSelecaoCard(
    bebida: BebidaEntity,
    selecionada: Boolean,
    onAlterarSelecao: () -> Unit
) {

    val habilitada =
        bebida.ativo


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled =
                    habilitada,

                onClick =
                    onAlterarSelecao
            ),

        shape =
            RoundedCornerShape(
                15.dp
            ),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    when {

                        selecionada ->

                            AdminGreen.copy(
                                alpha =
                                    0.055f
                            )

                        !habilitada ->

                            AdminSoft.copy(
                                alpha =
                                    0.7f
                            )

                        else ->

                            AdminSurface
                    }
            ),

        border =
            BorderStroke(
                width =
                    if (selecionada) {
                        1.5.dp
                    } else {
                        1.dp
                    },

                color =
                    if (selecionada) {

                        AdminGreen.copy(
                            alpha =
                                0.50f
                        )

                    } else {

                        AdminBorder
                    }
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    15.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            /*
             * =================================================
             * FOTO
             * =================================================
             */

            Box(
                modifier = Modifier
                    .size(
                        86.dp
                    )
                    .clip(
                        RoundedCornerShape(
                            12.dp
                        )
                    )
                    .background(
                        AdminSoft
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                val caminhoImagem =
                    bebida.imagemPath


                if (
                    !caminhoImagem
                        .isNullOrBlank()
                ) {

                    val arquivo =
                        File(
                            caminhoImagem
                        )


                    if (arquivo.exists()) {

                        AsyncImage(
                            model =
                                arquivo,

                            contentDescription =
                                bebida.nome,

                            modifier =
                                Modifier.fillMaxSize(),

                            contentScale =
                                ContentScale.Crop
                        )

                    } else {

                        SemImagemSelecao()
                    }

                } else {

                    SemImagemSelecao()
                }
            }


            Spacer(
                modifier =
                    Modifier.width(
                        18.dp
                    )
            )


            /*
             * =================================================
             * INFORMAÇÕES
             * =================================================
             */

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text =
                            bebida.nome,

                        fontSize =
                            18.sp,

                        fontWeight =
                            FontWeight.SemiBold,

                        color =
                            if (habilitada) {
                                AdminText
                            } else {
                                AdminDisabled
                            },

                        maxLines =
                            1,

                        overflow =
                            TextOverflow.Ellipsis
                    )


                    Spacer(
                        modifier =
                            Modifier.width(
                                9.dp
                            )
                    )


                    StatusSelecaoBebida(
                        ativa =
                            bebida.ativo
                    )
                }


                if (
                    bebida
                        .descricao
                        .isNotBlank()
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(
                                6.dp
                            )
                    )


                    Text(
                        text =
                            bebida.descricao,

                        fontSize =
                            13.sp,

                        lineHeight =
                            19.sp,

                        color =
                            if (habilitada) {
                                AdminTextSecondary
                            } else {
                                AdminDisabled
                            },

                        maxLines =
                            2,

                        overflow =
                            TextOverflow.Ellipsis
                    )
                }


                if (!habilitada) {

                    Spacer(
                        modifier =
                            Modifier.height(
                                7.dp
                            )
                    )


                    Text(
                        text =
                            "Ative esta bebida no catálogo para poder adicioná-la.",

                        fontSize =
                            11.sp,

                        color =
                            AdminDisabled
                    )
                }
            }


            Spacer(
                modifier =
                    Modifier.width(
                        18.dp
                    )
            )


            /*
             * =================================================
             * SELEÇÃO
             * =================================================
             */

            Column(
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Checkbox(
                    checked =
                        selecionada,

                    onCheckedChange = {

                        if (habilitada) {

                            onAlterarSelecao()
                        }
                    },

                    enabled =
                        habilitada
                )


                Text(
                    text =
                        if (selecionada) {
                            "Incluída"
                        } else {
                            "Adicionar"
                        },

                    fontSize =
                        10.sp,

                    fontWeight =
                        if (selecionada) {
                            FontWeight.SemiBold
                        } else {
                            FontWeight.Normal
                        },

                    color =
                        when {

                            selecionada ->
                                AdminGreen

                            habilitada ->
                                AdminTextSecondary

                            else ->
                                AdminDisabled
                        }
                )
            }
        }
    }
}


@Composable
private fun StatusSelecaoBebida(
    ativa: Boolean
) {

    val cor =
        if (ativa) {
            AdminGreen
        } else {
            AdminDisabled
        }


    Box(
        modifier = Modifier
            .background(
                color =
                    cor.copy(
                        alpha =
                            0.10f
                    ),

                shape =
                    RoundedCornerShape(
                        20.dp
                    )
            )
            .padding(
                horizontal =
                    8.dp,

                vertical =
                    3.dp
            )
    ) {

        Text(
            text =
                if (ativa) {
                    "ATIVA"
                } else {
                    "INATIVA"
                },

            fontSize =
                8.sp,

            letterSpacing =
                0.6.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                cor
        )
    }
}


@Composable
private fun SemImagemSelecao() {

    Text(
        text =
            "Sem foto",

        fontSize =
            10.sp,

        color =
            AdminTextSecondary
    )
}


@Composable
private fun EstadoVazioSelecao(
    titulo: String,
    descricao: String
) {

    Box(
        modifier =
            Modifier.fillMaxSize(),

        contentAlignment =
            Alignment.Center
    ) {

        Card(
            colors =
                CardDefaults.cardColors(
                    containerColor =
                        AdminSurface
                ),

            border =
                BorderStroke(
                    width =
                        1.dp,

                    color =
                        AdminBorder
                ),

            shape =
                RoundedCornerShape(
                    18.dp
                )
        ) {

            Column(
                modifier =
                    Modifier.padding(
                        38.dp
                    ),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text =
                        titulo,

                    fontSize =
                        20.sp,

                    fontWeight =
                        FontWeight.SemiBold,

                    color =
                        AdminText
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            7.dp
                        )
                )


                Text(
                    text =
                        descricao,

                    fontSize =
                        13.sp,

                    color =
                        AdminTextSecondary
                )
            }
        }
    }
}


@Composable
private fun BarraSalvarCardapio(
    quantidadeSelecionadas: Int,
    onCancelar: () -> Unit,
    onSalvar: () -> Unit
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                topStart =
                    18.dp,

                topEnd =
                    18.dp
            ),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    AdminSurface
            ),

        border =
            BorderStroke(
                width =
                    1.dp,

                color =
                    AdminBorder
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal =
                        42.dp,

                    vertical =
                        16.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text =
                        "$quantidadeSelecionadas bebida(s) selecionada(s)",

                    fontSize =
                        15.sp,

                    fontWeight =
                        FontWeight.SemiBold,

                    color =
                        AdminText
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            3.dp
                        )
                )


                Text(
                    text =
                        "Salve para aplicar esta seleção ao cardápio.",

                    fontSize =
                        12.sp,

                    color =
                        AdminTextSecondary
                )
            }


            TextButton(
                onClick =
                    onCancelar
            ) {

                Text(
                    text =
                        "Cancelar",

                    color =
                        AdminTextSecondary
                )
            }


            Spacer(
                modifier =
                    Modifier.width(
                        8.dp
                    )
            )


            Button(
                onClick =
                    onSalvar,

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            AdminDark,

                        contentColor =
                            AdminBackground
                    ),

                shape =
                    RoundedCornerShape(
                        10.dp
                    )
            ) {

                Text(
                    text =
                        "Salvar cardápio",

                    fontWeight =
                        FontWeight.SemiBold
                )
            }
        }
    }
}
