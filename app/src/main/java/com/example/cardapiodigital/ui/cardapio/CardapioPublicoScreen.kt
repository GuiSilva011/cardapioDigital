package com.example.cardapiodigital.ui.cardapio

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    Color(0xFF0A0A0A)

private val PretoCard =
    Color(0xFF141414)

private val PretoSecundario =
    Color(0xFF1B1B1B)

private val OffWhite =
    Color(0xFFE5E6E7)

private val TextoSecundario =
    Color(0xFFA8A8A8)

private val Dourado =
    Color(0xFFC6A15B)

private val DouradoSuave =
    Color(0xFF8F7442)


@Composable
fun CardapioPublicoScreen(
    viewModel: CardapioViewModel,
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
                                bebida = bebida
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun CardapioHeader(
    nomeCardapio: String,
    descricao: String,
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
                        OffWhite
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

                        color =
                            TextoSecundario
                    )
                }
            }


            /*
             * ACESSO ADMINISTRATIVO
             *
             * TEMPORÁRIO.
             *
             * Depois substituiremos
             * por acesso oculto + PIN.
             */
            Text(
                text =
                    "ADMIN",

                fontSize = 11.sp,

                letterSpacing = 1.5.sp,

                fontWeight =
                    FontWeight.Medium,

                color =
                    Dourado,

                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            10.dp
                        )
                    )
                    .clickable {

                        onAbrirAdministracao()
                    }
                    .background(
                        PretoSecundario
                    )
                    .padding(
                        horizontal = 16.dp,
                        vertical = 10.dp
                    )
            )
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
    bebida: BebidaEntity
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

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


                    if (
                        arquivo.exists()
                    ) {

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

                    color =
                        OffWhite,

                    maxLines = 2,

                    overflow =
                        TextOverflow.Ellipsis
                )


                if (
                    bebida
                        .descricao
                        .isNotBlank()
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(
                                9.dp
                            )
                    )


                    Text(
                        text =
                            bebida.descricao,

                        fontSize = 15.sp,

                        lineHeight = 22.sp,

                        fontWeight =
                            FontWeight.Normal,

                        color =
                            TextoSecundario,

                        maxLines = 4,

                        overflow =
                            TextOverflow.Ellipsis
                    )
                }
            }
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

            color =
                DouradoSuave
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

                color =
                    OffWhite,

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

                color =
                    TextoSecundario,

                textAlign =
                    TextAlign.Center
            )
        }
    }
}