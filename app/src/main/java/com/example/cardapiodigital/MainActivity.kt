package com.example.cardapiodigital

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cardapiodigital.ui.admin.AdminHomeScreen
import com.example.cardapiodigital.ui.admin.BebidasAdminScreen
import com.example.cardapiodigital.ui.admin.CardapiosAdminScreen
import com.example.cardapiodigital.ui.admin.SelecionarBebidasCardapioScreen
import com.example.cardapiodigital.ui.cardapio.CardapioPublicoScreen
import com.example.cardapiodigital.ui.theme.CardapioDigitalTheme
import com.example.cardapiodigital.viewmodel.BebidaViewModel
import com.example.cardapiodigital.viewmodel.CardapioViewModel


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            CardapioDigitalTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    AplicativoCardapio()
                }
            }
        }
    }
}


@Composable
fun AplicativoCardapio() {

    /*
     * VIEWMODELS
     */
    val bebidaViewModel: BebidaViewModel =
        viewModel()

    val cardapioViewModel: CardapioViewModel =
        viewModel()


    /*
     * TELA ATUAL
     */
    var telaAtual by rememberSaveable {
        mutableStateOf("inicio")
    }


    /*
     * CARDÁPIO SELECIONADO PARA
     * GERENCIAMENTO DAS BEBIDAS
     */
    var cardapioSelecionadoId by rememberSaveable {
        mutableStateOf(0L)
    }

    var cardapioSelecionadoNome by rememberSaveable {
        mutableStateOf("")
    }


    /*
     * NAVEGAÇÃO
     */
    when (telaAtual) {

        /*
         * =================================================
         * TELA INICIAL
         * =================================================
         */
        "inicio" -> {

            TelaInicial(

                /*
                 * Agora o botão Visualizar cardápio
                 * realmente abre a tela pública.
                 */
                onAbrirCardapio = {
                    telaAtual = "cardapio_publico"
                },

                onAbrirAdministracao = {
                    telaAtual = "admin"
                }
            )
        }


        /*
         * =================================================
         * CARDÁPIO PÚBLICO
         * =================================================
         *
         * Essa é a tela que o cliente verá.
         */
        "cardapio_publico" -> {

            CardapioPublicoScreen(
                viewModel = cardapioViewModel,

                onAbrirAdministracao = {
                    telaAtual = "admin"
                }
            )
        }


        /*
         * =================================================
         * HOME ADMINISTRATIVA
         * =================================================
         */
        "admin" -> {

            AdminHomeScreen(

                onAbrirBebidas = {
                    telaAtual = "bebidas"
                },

                onAbrirCardapios = {
                    telaAtual = "cardapios"
                },

                /*
                 * O botão "Exibir cardápio"
                 * do painel agora abre diretamente
                 * o cardápio público.
                 */
                onSair = {
                    telaAtual = "cardapio_publico"
                }
            )
        }


        /*
         * =================================================
         * GERENCIAMENTO DE BEBIDAS
         * =================================================
         */
        "bebidas" -> {

            BebidasAdminScreen(
                viewModel = bebidaViewModel,

                onVoltar = {
                    telaAtual = "admin"
                }
            )
        }


        /*
         * =================================================
         * GERENCIAMENTO DE CARDÁPIOS
         * =================================================
         */
        "cardapios" -> {

            CardapiosAdminScreen(
                viewModel = cardapioViewModel,

                onVoltar = {
                    telaAtual = "admin"
                },

                onGerenciarBebidas = { cardapio ->

                    cardapioSelecionadoId =
                        cardapio.id

                    cardapioSelecionadoNome =
                        cardapio.nome

                    telaAtual =
                        "selecionar_bebidas"
                }
            )
        }


        /*
         * =================================================
         * SELEÇÃO DAS BEBIDAS DO CARDÁPIO
         * =================================================
         */
        "selecionar_bebidas" -> {

            SelecionarBebidasCardapioScreen(
                cardapioId =
                    cardapioSelecionadoId,

                nomeCardapio =
                    cardapioSelecionadoNome,

                viewModel =
                    cardapioViewModel,

                onVoltar = {
                    telaAtual = "cardapios"
                }
            )
        }
    }
}


@Composable
private fun TelaInicial(
    onAbrirCardapio: () -> Unit,
    onAbrirAdministracao: () -> Unit,
    modifier: Modifier = Modifier
) {

    BoxWithConstraints(
        modifier = modifier
    ) {

        val layoutTablet =
            maxWidth >= 700.dp


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    PaddingValues(
                        horizontal =
                            if (layoutTablet) {
                                48.dp
                            } else {
                                24.dp
                            },

                        vertical = 32.dp
                    )
                ),

            verticalArrangement =
                Arrangement.spacedBy(28.dp)
        ) {

            /*
             * CABEÇALHO
             */
            Column(
                verticalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = "Cardápio Digital",

                    fontSize = 34.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        MaterialTheme
                            .colorScheme
                            .onBackground
                )


                Text(
                    text =
                        "Protótipo offline para gerenciamento e apresentação de bebidas.",

                    fontSize = 17.sp,

                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant
                )
            }


            /*
             * TABLET / TELA LARGA
             */
            if (layoutTablet) {

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.spacedBy(24.dp)
                ) {

                    /*
                     * VISUALIZAR CARDÁPIO
                     */
                    OpcaoInicial(
                        titulo =
                            "Visualizar cardápio",

                        descricao =
                            "Modo de apresentação utilizado pelos clientes.",

                        textoBotao =
                            "Abrir cardápio",

                        modifier =
                            Modifier.weight(1f),

                        onClick =
                            onAbrirCardapio
                    )


                    /*
                     * ADMINISTRAÇÃO
                     */
                    OpcaoInicial(
                        titulo =
                            "Painel administrativo",

                        descricao =
                            "Cadastro de bebidas e gerenciamento dos cardápios.",

                        textoBotao =
                            "Abrir administração",

                        modifier =
                            Modifier.weight(1f),

                        onClick =
                            onAbrirAdministracao
                    )
                }

            } else {

                /*
                 * TELA MAIS ESTREITA
                 */
                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(20.dp)
                ) {

                    /*
                     * VISUALIZAR CARDÁPIO
                     */
                    OpcaoInicial(
                        titulo =
                            "Visualizar cardápio",

                        descricao =
                            "Modo de apresentação utilizado pelos clientes.",

                        textoBotao =
                            "Abrir cardápio",

                        modifier =
                            Modifier.fillMaxWidth(),

                        onClick =
                            onAbrirCardapio
                    )


                    /*
                     * ADMINISTRAÇÃO
                     */
                    OpcaoInicial(
                        titulo =
                            "Painel administrativo",

                        descricao =
                            "Cadastro de bebidas e gerenciamento dos cardápios.",

                        textoBotao =
                            "Abrir administração",

                        modifier =
                            Modifier.fillMaxWidth(),

                        onClick =
                            onAbrirAdministracao
                    )
                }
            }
        }
    }
}


@Composable
private fun OpcaoInicial(
    titulo: String,
    descricao: String,
    textoBotao: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {

    Card(
        modifier = modifier
            .heightIn(
                min = 220.dp
            ),

        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme
                    .colorScheme
                    .surfaceContainer
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),

            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = titulo,

                fontSize = 24.sp,

                fontWeight =
                    FontWeight.SemiBold
            )


            Text(
                text = descricao,

                fontSize = 16.sp,

                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
            )


            Button(
                onClick = onClick
            ) {

                Text(
                    text = textoBotao
                )
            }
        }
    }
}


@Preview(
    name = "Tablet",
    widthDp = 1280,
    heightDp = 800,
    showBackground = true
)
@Composable
private fun TelaInicialTabletPreview() {

    CardapioDigitalTheme {

        TelaInicial(
            onAbrirCardapio = {},
            onAbrirAdministracao = {}
        )
    }
}


@Preview(
    name = "Tablet vertical",
    widthDp = 800,
    heightDp = 1280,
    showBackground = true
)
@Composable
private fun TelaInicialVerticalPreview() {

    CardapioDigitalTheme {

        TelaInicial(
            onAbrirCardapio = {},
            onAbrirAdministracao = {}
        )
    }
}