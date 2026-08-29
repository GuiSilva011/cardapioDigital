package com.example.cardapiodigital

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cardapiodigital.data.security.AdminAccessDialog
import com.example.cardapiodigital.data.security.AdminPinStore
import com.example.cardapiodigital.data.security.AlterarAdminPinDialog
import com.example.cardapiodigital.ui.admin.AdminHomeScreen
import com.example.cardapiodigital.ui.admin.BebidasAdminScreen
import com.example.cardapiodigital.ui.admin.CardapiosAdminScreen
import com.example.cardapiodigital.ui.admin.SelecionarBebidasCardapioScreen
import com.example.cardapiodigital.ui.cardapio.CardapioPublicoScreen
import com.example.cardapiodigital.ui.theme.CardapioDigitalTheme
import com.example.cardapiodigital.viewmodel.BebidaViewModel
import com.example.cardapiodigital.viewmodel.CardapioViewModel


private val MenuBackground =
    Color(0xFF050505)

private val MenuCard =
    Color(0xFF111111)

private val MenuGold =
    Color(0xFFC6A15B)

private val MenuGoldSoft =
    Color(0xFF9F8145)

private val MenuWhite =
    Color(0xFFFFFFFF)


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

    val context =
        LocalContext.current

    val adminPinStore =
        remember {
            AdminPinStore(
                context.applicationContext
            )
        }

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

    var adminDesbloqueado by rememberSaveable {
        mutableStateOf(false)
    }

    var pinConfigurado by rememberSaveable {
        mutableStateOf(
            adminPinStore.possuiPin()
        )
    }

    var mostrarDialogAcessoAdmin by rememberSaveable {
        mutableStateOf(false)
    }

    var mostrarDialogAlterarPin by rememberSaveable {
        mutableStateOf(false)
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
    BackHandler(
        enabled =
            telaAtual != "inicio"
    ) {

        telaAtual =
            when (telaAtual) {
                "bebidas",
                "cardapios" -> "admin"

                "selecionar_bebidas" -> "cardapios"

                "admin" -> {
                    adminDesbloqueado = false
                    "inicio"
                }

                else -> "inicio"
            }
    }


    when (telaAtual) {

        /*
         * =================================================
         * TELA INICIAL
         * =================================================
         */
        "inicio" -> {

            TelaInicial(

                /*
                 * A opção Visualizar cardápio
                 * abre a tela pública.
                 */
                onAbrirCardapio = {
                    telaAtual = "cardapio_publico"
                },

                onAbrirAdministracao = {
                    mostrarDialogAcessoAdmin = true
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

                onVoltarMenu = {
                    telaAtual = "inicio"
                },

                onAbrirAdministracao = {
                    mostrarDialogAcessoAdmin = true
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

                onVoltarMenu = {
                    adminDesbloqueado = false
                    telaAtual = "inicio"
                },

                onExibirCardapio = {
                    adminDesbloqueado = false
                    telaAtual = "cardapio_publico"
                },

                onAlterarPin = {
                    mostrarDialogAlterarPin = true
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

    if (mostrarDialogAcessoAdmin) {
        AdminAccessDialog(
            pinConfigurado =
                pinConfigurado,

            onDismiss = {
                mostrarDialogAcessoAdmin = false
            },

            onValidarPin = { pin ->
                adminPinStore.validarPin(pin)
            },

            onCriarPin = { pin ->
                adminPinStore.salvarPin(pin)
                pinConfigurado = true

                Toast.makeText(
                    context,
                    "PIN administrativo criado.",
                    Toast.LENGTH_SHORT
                ).show()
            },

            onAcessoLiberado = {
                adminDesbloqueado = true
                mostrarDialogAcessoAdmin = false
                telaAtual = "admin"
            }
        )
    }

    if (
        mostrarDialogAlterarPin &&
        adminDesbloqueado
    ) {
        AlterarAdminPinDialog(
            onDismiss = {
                mostrarDialogAlterarPin = false
            },

            onAlterarPin = { pinAtual, novoPin ->
                if (
                    adminPinStore.validarPin(
                        pinAtual
                    )
                ) {
                    adminPinStore.salvarPin(
                        novoPin
                    )

                    true
                } else {
                    false
                }
            },

            onPinAlterado = {
                mostrarDialogAlterarPin = false

                Toast.makeText(
                    context,
                    "PIN alterado com sucesso.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
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
            .fillMaxSize()
            .background(
                MenuBackground
            )
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

                    color = MenuGold
                )


                Text(
                    text =
                        "Protótipo offline para gerenciamento e apresentação de bebidas.",

                    fontSize = 17.sp,

                    color = MenuWhite
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

                        imagemRes =
                            R.drawable.cardapio_bebida,

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

                        imagemRes =
                            R.drawable.admin,

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

                        imagemRes =
                            R.drawable.cardapio_bebida,

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

                        imagemRes =
                            R.drawable.admin,

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
    imagemRes: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {

    Card(
        modifier = modifier
            .heightIn(
                min = 250.dp
            )
            .clickable(
                onClick = onClick
            ),

        shape =
            RoundedCornerShape(
                20.dp
            ),

        colors = CardDefaults.cardColors(
            containerColor =
                MenuCard
        ),

        border = BorderStroke(
            width = 1.dp,
            color = MenuGoldSoft.copy(
                alpha = 0.55f
            )
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),

            verticalArrangement =
                Arrangement.spacedBy(14.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(112.dp)
                    .background(
                        color = MenuGold.copy(
                            alpha = 0.10f
                        ),
                        shape = RoundedCornerShape(
                            24.dp
                        )
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                Image(
                    painter = painterResource(
                        id = imagemRes
                    ),

                    contentDescription = titulo,

                    modifier =
                        Modifier.size(78.dp),

                    contentScale =
                        ContentScale.Fit,

                    colorFilter =
                        ColorFilter.tint(
                            MenuGold
                        )
                )
            }

            Text(
                text = titulo,

                fontSize = 24.sp,

                fontWeight =
                    FontWeight.SemiBold,

                color = MenuGold,

                textAlign =
                    TextAlign.Center
            )


            Text(
                text = descricao,

                fontSize = 16.sp,

                color = MenuWhite,

                textAlign =
                    TextAlign.Center
            )
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
