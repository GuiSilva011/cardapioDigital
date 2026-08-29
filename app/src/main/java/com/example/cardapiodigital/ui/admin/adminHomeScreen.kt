package com.example.cardapiodigital.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardapiodigital.R


/*
 * =========================================================
 * PALETA DO PAINEL ADMINISTRATIVO
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


@Composable
fun AdminHomeScreen(
    onAbrirBebidas: () -> Unit,
    onAbrirCardapios: () -> Unit,
    onVoltarMenu: () -> Unit,
    onExibirCardapio: () -> Unit,
    onAlterarPin: () -> Unit
) {

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                AdminBackground
            )
    ) {

        val layoutLargo =
            maxWidth >= 800.dp


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal =
                        if (layoutLargo) {
                            48.dp
                        } else {
                            24.dp
                        },

                    vertical = 32.dp
                )
        ) {

            /*
             * =================================================
             * CABEÇALHO
             * =================================================
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
                        text =
                            "PAINEL ADMINISTRATIVO",

                        fontSize = 12.sp,

                        letterSpacing = 1.8.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            AdminGold
                    )


                    Spacer(
                        modifier =
                            Modifier.height(
                                8.dp
                            )
                    )


                    Text(
                        text =
                            "Gerenciamento",

                        fontSize =
                            if (layoutLargo) {
                                34.sp
                            } else {
                                28.sp
                            },

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
                            "Gerencie as bebidas e os cardápios exibidos no tablet.",

                        fontSize = 15.sp,

                        color =
                            AdminTextSecondary
                    )
                }


                Row(
                    horizontalArrangement =
                        Arrangement.spacedBy(10.dp),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    TextButton(
                        onClick =
                            onVoltarMenu
                    ) {

                        Text(
                            text =
                                "← Menu principal",

                            color =
                                AdminTextSecondary
                        )
                    }


                    Button(
                        onClick =
                            onExibirCardapio,

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    AdminDark,

                                contentColor =
                                    AdminBackground
                            ),

                        shape =
                            RoundedCornerShape(12.dp)
                    ) {

                        Text(
                            text =
                                "Exibir cardápio",

                            fontWeight =
                                FontWeight.SemiBold
                        )
                    }
                }
            }


            Spacer(
                modifier =
                    Modifier.height(
                        28.dp
                    )
            )


            HorizontalDivider(
                color =
                    AdminBorder
            )


            Spacer(
                modifier =
                    Modifier.height(
                        30.dp
                    )
            )


            /*
             * =================================================
             * VISÃO GERAL
             * =================================================
             */

            Text(
                text =
                    "VISÃO GERAL",

                fontSize = 12.sp,

                letterSpacing = 1.5.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    AdminTextSecondary
            )


            Spacer(
                modifier =
                    Modifier.height(
                        16.dp
                    )
            )


            /*
             * =================================================
             * CARDS PRINCIPAIS
             * =================================================
             */

            if (layoutLargo) {

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.spacedBy(
                            22.dp
                        )
                ) {

                    AdminOpcaoCard(
                        titulo =
                            "Bebidas",

                        descricao =
                            "Cadastre fotos, nomes e descrições das bebidas disponíveis.",

                        textoBotao =
                            "Gerenciar bebidas",

                        imageRes =
                            R.drawable.bg_bebidas,

                        onClick =
                            onAbrirBebidas,

                        modifier =
                            Modifier.weight(1f)
                    )


                    AdminOpcaoCard(
                        titulo =
                            "Menus personalizados",

                        descricao =
                            "Gerencie o cardápio fixo e crie versões personalizadas para eventos.",

                        textoBotao =
                            "Gerenciar cardápios",

                        imageRes =
                            R.drawable.bg_cardapio,

                        onClick =
                            onAbrirCardapios,

                        modifier =
                            Modifier.weight(1f)
                    )
                }

            } else {

                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(
                            18.dp
                        )
                ) {

                    AdminOpcaoCard(
                        titulo =
                            "Bebidas",

                        descricao =
                            "Cadastre fotos, nomes e descrições das bebidas disponíveis.",

                        textoBotao =
                            "Gerenciar bebidas",

                        imageRes =
                            R.drawable.bg_bebidas,

                        onClick =
                            onAbrirBebidas,

                        modifier =
                            Modifier.fillMaxWidth()
                    )


                    AdminOpcaoCard(
                        titulo =
                            "Menus personalizados",

                        descricao =
                            "Gerencie o cardápio fixo e crie versões personalizadas para eventos.",

                        textoBotao =
                            "Gerenciar cardápios",

                        imageRes =
                            R.drawable.bg_cardapio,

                        onClick =
                            onAbrirCardapios,

                        modifier =
                            Modifier.fillMaxWidth()
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
             * =================================================
             * INFORMAÇÃO INFERIOR
             * =================================================
             */

            Card(
                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(
                        16.dp
                    ),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            AdminSoft
                    ),

                border =
                    BorderStroke(
                        width = 1.dp,
                        color =
                            AdminBorder
                    )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 22.dp,
                            vertical = 18.dp
                        ),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .background(
                                color =
                                    AdminGold.copy(
                                        alpha = 0.12f
                                    ),

                                shape =
                                    RoundedCornerShape(
                                        10.dp
                                    )
                            )
                            .padding(
                                horizontal = 13.dp,
                                vertical = 9.dp
                            )
                    ) {

                        Text(
                            text = "OFFLINE",

                            fontSize = 11.sp,

                            letterSpacing =
                                1.sp,

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                AdminGold
                        )
                    }


                    Spacer(
                        modifier =
                            Modifier.padding(
                                horizontal =
                                    7.dp
                            )
                    )


                    Column(
                        modifier =
                            Modifier.weight(1f)
                    ) {

                        Text(
                            text =
                                "Dados armazenados no tablet",

                            fontSize = 15.sp,

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
                                "As alterações feitas neste painel ficam salvas localmente e não dependem de internet.",

                            fontSize = 13.sp,

                            color =
                                AdminTextSecondary
                        )
                    }


                    TextButton(
                        onClick =
                            onAlterarPin
                    ) {

                        Text(
                            text =
                                "Alterar PIN",

                            fontWeight =
                                FontWeight.SemiBold,

                            color =
                                AdminGold
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun AdminOpcaoCard(
    titulo: String,
    descricao: String,
    textoBotao: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageRes: Int? = null
) {

    Card(
        modifier =
            modifier.heightIn(
                min = 275.dp
            ),

        shape =
            RoundedCornerShape(
                18.dp
            ),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    AdminSurface
            ),

        border =
            BorderStroke(
                width = 1.dp,
                color = AdminGold.copy(
                    alpha = 0.45f
                )
            )
    ) {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            if (imageRes != null) {

                Image(
                    painter =
                        painterResource(
                            id = imageRes
                        ),

                    contentDescription =
                        null,

                    modifier =
                        Modifier.fillMaxSize(),

                    contentScale =
                        ContentScale.Crop
                )


                // Overlay para legibilidade
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Color.Black.copy(
                                alpha = 0.55f
                            )
                        )
                )
            }


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        26.dp
                    )
            ) {

                /*
                 * Título
                 */
                Text(
                    text =
                        titulo,

                    fontSize = 26.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        if (imageRes == null)
                            AdminText
                        else
                            AdminGold
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            10.dp
                        )
                )


                /*
                 * Descrição
                 */
                Text(
                    text =
                        descricao,

                    fontSize = 15.sp,

                    lineHeight =
                        22.sp,

                    color =
                        if (imageRes == null)
                            AdminTextSecondary
                        else
                            Color.White.copy(
                                alpha = 0.85f
                            )
                )


                Spacer(
                    modifier =
                        Modifier.weight(1f)
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            24.dp
                        )
                )


                /*
                 * Botão
                 */
                Button(
                    onClick =
                        onClick,

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = AdminGold,
                            contentColor = AdminBackground
                        ),

                    shape =
                        RoundedCornerShape(
                            11.dp
                        )
                ) {

                    Text(
                        text =
                            textoBotao,

                        fontWeight =
                            FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
