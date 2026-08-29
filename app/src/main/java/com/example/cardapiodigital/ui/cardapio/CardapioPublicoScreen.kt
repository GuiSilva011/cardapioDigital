package com.example.cardapiodigital.ui.cardapio

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
 * PALETA EXCLUSIVA DO CARDÁPIO PÚBLICO
 * =========================================================
 *
 * Não estamos alterando o Theme.kt.
 * Portanto o painel administrativo continua intacto.
 */

private val PretoPrincipal =
    Color(0xFF050505)

private val PretoCard =
    Color(0xFF111111)

private val PretoSecundario =
    Color(0xFF191919)

private val Branco =
    Color(0xFFFFFFFF)

private val Dourado =
    Color(0xFFC6A15B)

private val DouradoSuave =
    Color(0xFF9F8145)


@Composable
fun CardapioPublicoScreen(
    viewModel: CardapioViewModel,
    onVoltarMenu: () -> Unit,
    onAbrirAdministracao: () -> Unit
) {

    val cardapioAtivo by
    viewModel
        .cardapioAtivo
        .collectAsStateWithLifecycle()

    val bebidas by
    viewModel
        .bebidasCardapioAtivo
        .collectAsStateWithLifecycle()

    var bebidaSelecionada by remember {
        mutableStateOf<BebidaEntity?>(null)
    }


    BackHandler(
        enabled =
            bebidaSelecionada != null
    ) {
        bebidaSelecionada = null
    }


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                PretoPrincipal
            )
    ) {

        /*
         * Quantidade de colunas conforme
         * o espaço disponível no tablet.
         */
        val quantidadeColunas =
            when {

                maxWidth >= 1200.dp ->
                    4

                maxWidth >= 840.dp ->
                    3

                else ->
                    2
            }


        AnimatedContent(
            targetState =
                bebidaSelecionada,

            transitionSpec = {

                if (targetState != null) {

                    (
                            slideInHorizontally(
                                animationSpec = tween(320)
                            ) { largura ->
                                largura / 5
                            } +
                                    fadeIn(
                                        animationSpec = tween(
                                            durationMillis = 260,
                                            delayMillis = 40
                                        )
                                    )
                            ) togetherWith
                            (
                                    slideOutHorizontally(
                                        animationSpec = tween(220)
                                    ) { largura ->
                                        -largura / 6
                                    } +
                                            fadeOut(
                                                animationSpec = tween(180)
                                            )
                                    )

                } else {

                    (
                            slideInHorizontally(
                                animationSpec = tween(300)
                            ) { largura ->
                                -largura / 5
                            } +
                                    fadeIn(
                                        animationSpec = tween(
                                            durationMillis = 240,
                                            delayMillis = 30
                                        )
                                    )
                            ) togetherWith
                            (
                                    slideOutHorizontally(
                                        animationSpec = tween(220)
                                    ) { largura ->
                                        largura / 6
                                    } +
                                            fadeOut(
                                                animationSpec = tween(180)
                                            )
                                    )
                }
            },

            label =
                "transicao_cardapio_detalhe"
        ) { bebidaEmDetalhe ->

            if (bebidaEmDetalhe == null) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            PretoPrincipal
                        )
                ) {

                    /*
                     * CABEÇALHO
                     */
                    CardapioHeader(
                        nomeCardapio =
                            cardapioAtivo?.nome
                                ?: "Cardápio",

                        descricao =
                            cardapioAtivo?.descricao
                                ?: "",

                        onVoltarMenu =
                            onVoltarMenu,

                        onAbrirAdministracao =
                            onAbrirAdministracao
                    )


                    /*
                     * CONTEÚDO
                     */
                    when {

                        cardapioAtivo == null -> {

                            EstadoVazio(
                                titulo =
                                    "Nenhum cardápio ativo",

                                descricao =
                                    "Selecione um cardápio no painel administrativo."
                            )
                        }


                        bebidas.isEmpty() -> {

                            EstadoVazio(
                                titulo =
                                    "Cardápio em preparação",

                                descricao =
                                    "As bebidas deste cardápio ainda não foram adicionadas."
                            )
                        }


                        else -> {

                            LazyVerticalGrid(
                                columns =
                                    GridCells.Fixed(
                                        quantidadeColunas
                                    ),

                                modifier =
                                    Modifier.fillMaxSize(),

                                contentPadding =
                                    PaddingValues(
                                        start = 42.dp,
                                        end = 42.dp,
                                        top = 18.dp,
                                        bottom = 48.dp
                                    ),

                                horizontalArrangement =
                                    Arrangement.spacedBy(
                                        22.dp
                                    ),

                                verticalArrangement =
                                    Arrangement.spacedBy(
                                        26.dp
                                    )
                            ) {

                                items(
                                    items = bebidas,

                                    key = { bebida ->
                                        bebida.id
                                    }
                                ) { bebida ->

                                    BebidaCard(
                                        bebida = bebida,

                                        onClick = {
                                            if (bebidaSelecionada == null) {
                                                bebidaSelecionada = bebida
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

            } else {

                DetalheBebidaScreen(
                    bebida = bebidaEmDetalhe,

                    onVoltar = {
                        bebidaSelecionada = null
                    }
                )
            }
        }
    }
}


@Composable
private fun CardapioHeader(
    nomeCardapio: String,
    descricao: String,
    onVoltarMenu: () -> Unit,
    onAbrirAdministracao: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                PretoPrincipal
            )
            .padding(
                start = 44.dp,
                end = 44.dp,
                top = 36.dp,
                bottom = 22.dp
            )
    ) {

        /*
         * TOPO
         */
        Row(
            modifier =
                Modifier.fillMaxWidth(),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text = "MENU DE BEBIDAS",

                    fontSize = 12.sp,

                    letterSpacing = 2.4.sp,

                    fontWeight =
                        FontWeight.Medium,

                    color =
                        Dourado
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )


                Text(
                    text =
                        nomeCardapio,

                    fontSize = 40.sp,

                    lineHeight = 46.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        Dourado
                )


                if (
                    descricao
                        .isNotBlank()
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(
                                8.dp
                            )
                    )


                    Text(
                        text =
                            descricao,

                        fontSize = 16.sp,

                        lineHeight = 23.sp,

                        color = Branco
                    )
                }
            }


            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(10.dp),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                AcaoCardapioHeader(
                    texto = "← MENU",
                    onClick = onVoltarMenu
                )


                AcaoCardapioHeader(
                    texto = "ADMIN",
                    onClick = onAbrirAdministracao
                )
            }
        }


        Spacer(
            modifier =
                Modifier.height(
                    26.dp
                )
        )


        /*
         * LINHA DOURADA
         */
        Row(
            modifier =
                Modifier.fillMaxWidth(),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .height(
                        1.dp
                    )
                    .weight(
                        0.15f
                    )
                    .background(
                        Dourado
                    )
            )


            Box(
                modifier = Modifier
                    .height(
                        1.dp
                    )
                    .weight(
                        0.85f
                    )
                    .background(
                        Color(
                            0xFF282828
                        )
                    )
            )
        }
    }
}


@Composable
private fun BebidaCard(
    bebida: BebidaEntity,
    onClick: () -> Unit
) {

    val interactionSource =
        remember {
            MutableInteractionSource()
        }

    val pressionado by
    interactionSource
        .collectIsPressedAsState()

    val escala by
    animateFloatAsState(
        targetValue =
            if (pressionado) {
                0.97f
            } else {
                1f
            },

        animationSpec =
            tween(120),

        label =
            "pressionar_card_bebida"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(escala)
            .clickable(
                interactionSource =
                    interactionSource,

                indication =
                    null,

                onClick = onClick
            ),

        shape =
            RoundedCornerShape(
                18.dp
            ),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    PretoCard
            ),

        border =
            BorderStroke(
                width = 1.dp,
                color =
                    Color(
                        0xFF252525
                    )
            )
    ) {

        Column {

            /*
             * IMAGEM
             */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(
                        1.18f
                    )
                    .background(
                        PretoSecundario
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                ImagemBebida(
                    bebida = bebida,
                    modifier = Modifier.fillMaxSize()
                )


                /*
                 * DETALHE DOURADO
                 * ABAIXO DA FOTO
                 */
                Box(
                    modifier = Modifier
                        .align(
                            Alignment.BottomStart
                        )
                        .fillMaxWidth()
                        .height(
                            2.dp
                        )
                        .background(
                            DouradoSuave
                        )
                )
            }


            /*
             * CONTEÚDO
             */
            Column(
                modifier =
                    Modifier.padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 20.dp,
                        bottom = 22.dp
                    )
            ) {

                Text(
                    text =
                        bebida.nome,

                    fontSize = 22.sp,

                    lineHeight = 28.sp,

                    fontWeight =
                        FontWeight.SemiBold,

                    color = Dourado,

                    maxLines = 2,

                    overflow =
                        TextOverflow.Ellipsis
                )


            }
        }
    }
}


@Composable
private fun DetalheBebidaScreen(
    bebida: BebidaEntity,
    onVoltar: () -> Unit
) {

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                PretoPrincipal
            )
    ) {

        val layoutLargo =
            maxWidth >= 700.dp


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 32.dp,
                    end = 32.dp,
                    top = 20.dp,
                    bottom = 18.dp
                )
        ) {

            Text(
                text = "← VOLTAR AO CARDÁPIO",

                fontSize = 11.sp,

                letterSpacing = 1.3.sp,

                fontWeight =
                    FontWeight.Medium,

                color =
                    Dourado,

                modifier = Modifier
                    .clip(
                        RoundedCornerShape(10.dp)
                    )
                    .clickable(
                        onClick = onVoltar
                    )
                    .background(
                        PretoSecundario
                    )
                    .padding(
                        horizontal = 16.dp,
                        vertical = 11.dp
                    )
            )


            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )


            if (layoutLargo) {

                Row(
                    modifier =
                        Modifier.fillMaxSize(),

                    horizontalArrangement =
                        Arrangement.spacedBy(36.dp)
                ) {

                    ImagemBebida(
                        bebida = bebida,

                        modifier = Modifier
                            .weight(0.42f)
                            .fillMaxHeight()
                            .clip(
                                RoundedCornerShape(20.dp)
                            )
                    )


                    Column(
                        modifier = Modifier
                            .weight(0.58f)
                            .fillMaxHeight()
                            .padding(
                                top = 28.dp,
                                bottom = 12.dp
                            ),

                        verticalArrangement =
                            Arrangement.Top
                    ) {

                        TituloBebida(
                            nome = bebida.nome
                        )


                        Spacer(
                            modifier =
                                Modifier.height(28.dp)
                        )


                        DescricaoBebidaCard(
                            bebida = bebida
                        )
                    }
                }

            } else {

                Column(
                    modifier =
                        Modifier.fillMaxSize()
                ) {

                    TituloBebida(
                        nome = bebida.nome
                    )


                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )


                    ImagemBebida(
                        bebida = bebida,

                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(
                                RoundedCornerShape(18.dp)
                            )
                    )


                    Spacer(
                        modifier =
                            Modifier.height(18.dp)
                    )


                    DescricaoBebidaCard(
                        bebida = bebida
                    )
                }
            }
        }
    }
}


@Composable
private fun TituloBebida(
    nome: String
) {

    Column(
        modifier =
            Modifier.width(IntrinsicSize.Max)
    ) {

        Text(
            text =
                nome,

            fontSize = 36.sp,

            lineHeight = 43.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                Dourado,

            maxLines = 2,

            overflow =
                TextOverflow.Ellipsis
        )


        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(
                    DouradoSuave
                )
        )
    }
}


@Composable
private fun DescricaoBebidaCard(
    bebida: BebidaEntity
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(14.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    PretoCard
            ),

        border =
            BorderStroke(
                width = 1.dp,
                color = DouradoSuave.copy(alpha = 0.55f)
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 18.dp
                )
        ) {

            Text(
                text =
                    "DESCRIÇÃO DA BEBIDA",

                fontSize = 15.sp,

                letterSpacing = 1.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Dourado
            )


            Spacer(
                modifier =
                    Modifier.height(9.dp)
            )


            Text(
                text =
                    bebida.descricao
                        .ifBlank {
                            "Sem descrição cadastrada."
                        },

                fontSize = 17.sp,

                lineHeight = 25.sp,

                color =
                    Branco
            )
        }
    }
}


@Composable
private fun ImagemBebida(
    bebida: BebidaEntity,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .background(
                PretoSecundario
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
                File(caminhoImagem)


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

                SemFoto()
            }

        } else {

            SemFoto()
        }
    }
}


@Composable
private fun SemFoto() {

    Column(
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text = "SEM IMAGEM",

            fontSize = 11.sp,

            letterSpacing =
                1.5.sp,

            fontWeight =
                FontWeight.Medium,

            color = Branco
        )
    }
}


@Composable
private fun EstadoVazio(
    titulo: String,
    descricao: String
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                PretoPrincipal
            ),

        contentAlignment =
            Alignment.Center
    ) {

        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally,

            modifier =
                Modifier.padding(
                    48.dp
                )
        ) {

            /*
             * DETALHE
             */
            Box(
                modifier = Modifier
                    .fillMaxWidth(
                        0.08f
                    )
                    .height(
                        2.dp
                    )
                    .background(
                        Dourado
                    )
            )


            Spacer(
                modifier =
                    Modifier.height(
                        22.dp
                    )
            )


            Text(
                text =
                    titulo,

                fontSize = 27.sp,

                fontWeight =
                    FontWeight.SemiBold,

                color = Dourado,

                textAlign =
                    TextAlign.Center
            )


            Spacer(
                modifier =
                    Modifier.height(
                        10.dp
                    )
            )


            Text(
                text =
                    descricao,

                fontSize = 16.sp,

                lineHeight = 23.sp,

                color = Branco,

                textAlign =
                    TextAlign.Center
            )
        }
    }
}


@Composable
private fun AcaoCardapioHeader(
    texto: String,
    onClick: () -> Unit
) {

    Text(
        text =
            texto,

        fontSize = 11.sp,

        letterSpacing = 1.2.sp,

        fontWeight =
            FontWeight.Medium,

        color =
            Dourado,

        modifier = Modifier
            .clip(
                RoundedCornerShape(10.dp)
            )
            .clickable(
                onClick = onClick
            )
            .background(
                PretoSecundario
            )
            .padding(
                horizontal = 16.dp,
                vertical = 10.dp
            )
    )
}
