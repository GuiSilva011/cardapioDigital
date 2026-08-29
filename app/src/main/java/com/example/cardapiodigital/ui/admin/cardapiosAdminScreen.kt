package com.example.cardapiodigital.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cardapiodigital.data.entity.CardapioEntity
import com.example.cardapiodigital.viewmodel.CardapioViewModel


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

private val AdminRed =
    Color(0xFFD45A5A)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardapiosAdminScreen(
    viewModel: CardapioViewModel,
    onVoltar: () -> Unit,
    onGerenciarBebidas: (CardapioEntity) -> Unit
) {

    val cardapios by
    viewModel.cardapios.collectAsStateWithLifecycle()

    var busca by remember {
        mutableStateOf("")
    }

    var mostrarFormulario by remember {
        mutableStateOf(false)
    }

    var cardapioEmEdicao by remember {
        mutableStateOf<CardapioEntity?>(null)
    }

    var cardapioParaExcluir by remember {
        mutableStateOf<CardapioEntity?>(null)
    }


    /*
     * FILTRO LOCAL
     */
    val cardapiosFiltrados =
        cardapios.filter { cardapio ->

            busca.isBlank() ||
                    cardapio.nome.contains(
                        busca,
                        ignoreCase = true
                    ) ||
                    cardapio.descricao.contains(
                        busca,
                        ignoreCase = true
                    )
        }


    val cardapioAtivo =
        cardapios.firstOrNull {
            it.ativo
        }


    val personalizados =
        cardapios.count {
            !it.fixo
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

                title = {

                    Text(
                        text =
                            "Gerenciamento de cardápios",

                        color =
                            AdminText,

                        fontWeight =
                            FontWeight.SemiBold
                    )
                },

                navigationIcon = {

                    TextButton(
                        onClick =
                            onVoltar
                    ) {

                        Text(
                            text =
                                "← Voltar",

                            color =
                                AdminText
                        )
                    }
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
                            "ORGANIZAÇÃO",

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
                            "Cardápios",

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
                            "Monte diferentes seleções de bebidas e escolha qual será exibida no tablet.",

                        fontSize = 15.sp,

                        color =
                            AdminTextSecondary
                    )
                }


                Button(
                    onClick = {

                        cardapioEmEdicao =
                            null

                        mostrarFormulario =
                            true
                    },

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                AdminDark,

                            contentColor =
                                AdminBackground
                        ),

                    shape =
                        RoundedCornerShape(
                            11.dp
                        )
                ) {

                    Text(
                        text =
                            "+ Novo cardápio",

                        fontWeight =
                            FontWeight.SemiBold
                    )
                }
            }


            Spacer(
                modifier =
                    Modifier.height(
                        26.dp
                    )
            )


            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                OutlinedTextField(
                    value =
                        busca,

                    onValueChange = {
                        busca = it
                    },

                    modifier =
                        Modifier.weight(1.35f),

                    label = {
                        Text("Buscar cardápio")
                    },

                    placeholder = {
                        Text("Nome ou descrição")
                    },

                    singleLine =
                        true,

                    shape =
                        RoundedCornerShape(12.dp)
                )


                Spacer(
                    modifier =
                        Modifier.width(16.dp)
                )


                ResumoCardapiosCompacto(
                    total =
                        cardapios.size,

                    personalizados =
                        personalizados,

                    ativo =
                        cardapioAtivo?.nome,

                    modifier =
                        Modifier.weight(1f)
                )
            }


            Spacer(
                modifier =
                    Modifier.height(
                        20.dp
                    )
            )


            /*
             * =================================================
             * LISTA
             * =================================================
             */

            when {

                cardapios.isEmpty() -> {

                    EstadoVazioCardapios(
                        onCadastrar = {

                            cardapioEmEdicao =
                                null

                            mostrarFormulario =
                                true
                        }
                    )
                }


                cardapiosFiltrados.isEmpty() -> {

                    Box(
                        modifier =
                            Modifier.fillMaxSize(),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        Column(
                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {

                            Text(
                                text =
                                    "Nenhum cardápio encontrado",

                                fontSize = 20.sp,

                                fontWeight =
                                    FontWeight.SemiBold,

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
                                    "Tente pesquisar por outro nome.",

                                color =
                                    AdminTextSecondary
                            )
                        }
                    }
                }


                else -> {

                    LazyColumn(
                        verticalArrangement =
                            Arrangement.spacedBy(
                                12.dp
                            )
                    ) {

                        items(
                            items =
                                cardapiosFiltrados,

                            key = {
                                    cardapio ->
                                cardapio.id
                            }
                        ) { cardapio ->

                            CardapioAdminCard(
                                cardapio =
                                    cardapio,

                                onAtivar = {

                                    viewModel
                                        .ativarCardapio(
                                            cardapio.id
                                        )
                                },

                                onGerenciarBebidas = {

                                    onGerenciarBebidas(
                                        cardapio
                                    )
                                },

                                onEditar = {

                                    cardapioEmEdicao =
                                        cardapio

                                    mostrarFormulario =
                                        true
                                },

                                onExcluir = {

                                    if (!cardapio.fixo) {

                                        cardapioParaExcluir =
                                            cardapio
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }


    /*
     * =====================================================
     * FORMULÁRIO
     * =====================================================
     */

    if (mostrarFormulario) {

        FormularioCardapioDialog(
            cardapio =
                cardapioEmEdicao,

            onCancelar = {

                mostrarFormulario =
                    false

                cardapioEmEdicao =
                    null
            },

            onSalvar = {
                    nome,
                    descricao ->

                val cardapioAtual =
                    cardapioEmEdicao


                if (cardapioAtual == null) {

                    viewModel
                        .criarCardapio(
                            nome =
                                nome,

                            descricao =
                                descricao
                        )

                } else {

                    viewModel
                        .editarCardapio(
                            cardapio =
                                cardapioAtual,

                            nome =
                                nome,

                            descricao =
                                descricao
                        )
                }


                mostrarFormulario =
                    false

                cardapioEmEdicao =
                    null
            }
        )
    }


    /*
     * =====================================================
     * EXCLUSÃO
     * =====================================================
     */

    cardapioParaExcluir?.let {
            cardapio ->

        AlertDialog(
            onDismissRequest = {

                cardapioParaExcluir =
                    null
            },

            title = {

                Text(
                    text =
                        "Excluir cardápio"
                )
            },

            text = {

                Text(
                    text =
                        "Deseja realmente excluir \"${cardapio.nome}\"?\n\nAs bebidas não serão excluídas do catálogo. Apenas este cardápio será removido."
                )
            },

            confirmButton = {

                Button(
                    onClick = {

                        viewModel
                            .excluirCardapio(
                                cardapio
                            )

                        cardapioParaExcluir =
                            null
                    },

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                AdminRed,

                            contentColor =
                                Color.White
                        )
                ) {

                    Text(
                        text =
                            "Excluir"
                    )
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {

                        cardapioParaExcluir =
                            null
                    }
                ) {

                    Text(
                        text =
                            "Cancelar"
                    )
                }
            }
        )
    }
}


@Composable
private fun ResumoCardapiosCompacto(
    total: Int,
    personalizados: Int,
    ativo: String?,
    modifier: Modifier = Modifier
) {

    Card(
        modifier =
            modifier,

        shape =
            RoundedCornerShape(12.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    AdminSurface
            ),

        border =
            BorderStroke(
                1.dp,
                AdminBorder.copy(alpha = 0.7f)
            )
    ) {

        Column(
            modifier =
                Modifier.padding(
                    horizontal = 18.dp,
                    vertical = 11.dp
                )
        ) {

            Text(
                text =
                    "$total cardápios  •  $personalizados personalizados",

                fontSize = 12.sp,

                color =
                    AdminTextSecondary
            )


            Spacer(
                modifier =
                    Modifier.height(4.dp)
            )


            Text(
                text =
                    "Ativo: ${ativo ?: "nenhum"}",

                fontSize = 13.sp,

                fontWeight =
                    FontWeight.SemiBold,

                color =
                    if (ativo == null) {
                        AdminTextSecondary
                    } else {
                        AdminGreen
                    },

                maxLines = 1,

                overflow =
                    TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
private fun CardapioAdminCard(
    cardapio: CardapioEntity,
    onAtivar: () -> Unit,
    onGerenciarBebidas: () -> Unit,
    onEditar: () -> Unit,
    onExcluir: () -> Unit
) {

    val ativo =
        cardapio.ativo

    var menuAberto by remember {
        mutableStateOf(false)
    }


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
                    if (ativo) {

                        Color(
                            0xFF101712
                        )

                    } else {

                        AdminSurface
                    }
            ),

        border =
            BorderStroke(
                width =
                    if (ativo) {
                        1.5.dp
                    } else {
                        1.dp
                    },

                color =
                    if (ativo) {

                        AdminGreen.copy(
                            alpha = 0.55f
                        )

                    } else {

                        AdminBorder
                    }
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    20.dp
                )
        ) {

            /*
             * PRIMEIRA LINHA
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

                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Text(
                            text =
                                cardapio.nome,

                            fontSize = 20.sp,

                            fontWeight =
                                FontWeight.SemiBold,

                            color =
                                AdminText,

                            maxLines =
                                1,

                            overflow =
                                TextOverflow.Ellipsis
                        )


                        Spacer(
                            modifier =
                                Modifier.width(
                                    10.dp
                                )
                        )


                        if (cardapio.fixo) {

                            BadgeCardapio(
                                texto =
                                    "FIXO",

                                cor =
                                    AdminGold
                            )


                            Spacer(
                                modifier =
                                    Modifier.width(
                                        7.dp
                                    )
                            )
                        }


                        if (ativo) {

                            BadgeCardapio(
                                texto =
                                    "ATIVO",

                                cor =
                                    AdminGreen
                            )
                        }
                    }


                    if (
                        cardapio
                            .descricao
                            .isNotBlank()
                    ) {

                        Spacer(
                            modifier =
                                Modifier.height(
                                    7.dp
                                )
                        )


                        Text(
                            text =
                                cardapio.descricao,

                            fontSize = 14.sp,

                            lineHeight =
                                20.sp,

                            color =
                                AdminTextSecondary,

                            maxLines =
                                2,

                            overflow =
                                TextOverflow.Ellipsis
                        )
                    }
                }


            }


            Spacer(
                modifier =
                    Modifier.height(
                        15.dp
                    )
            )


            /*
             * AÇÕES
             */
            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Button(
                    onClick =
                        onGerenciarBebidas,

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
                            "Abrir bebidas",

                        fontWeight =
                            FontWeight.SemiBold
                    )
                }


                Spacer(
                    modifier =
                        Modifier.width(
                            9.dp
                        )
                )


                /*
                 * ATIVAR
                 */
                if (!ativo) {

                    TextButton(
                        onClick =
                            onAtivar
                    ) {

                        Text(
                            text =
                                "Ativar",

                            color =
                                AdminGreen,

                            fontWeight =
                                FontWeight.SemiBold
                        )
                    }
                }


                Spacer(
                    modifier =
                        Modifier.weight(1f)
                )


                Box {
                    TextButton(
                        onClick = {
                            menuAberto = true
                        }
                    ) {

                        Text(
                            text =
                                "Mais",

                            color =
                                AdminTextSecondary
                        )
                    }


                    DropdownMenu(
                        expanded =
                            menuAberto,

                        onDismissRequest = {
                            menuAberto = false
                        }
                    ) {

                        DropdownMenuItem(
                            text = {
                                Text("Editar")
                            },

                            onClick = {
                                menuAberto = false
                                onEditar()
                            }
                        )


                        if (!cardapio.fixo) {

                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Excluir",
                                        color = AdminRed
                                    )
                                },

                                onClick = {
                                    menuAberto = false
                                    onExcluir()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun BadgeCardapio(
    texto: String,
    cor: Color
) {

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
                    9.dp,

                vertical =
                    4.dp
            )
    ) {

        Text(
            text =
                texto,

            fontSize = 9.sp,

            letterSpacing =
                0.7.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                cor
        )
    }
}


@Composable
private fun EstadoVazioCardapios(
    onCadastrar: () -> Unit
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

            shape =
                RoundedCornerShape(
                    18.dp
                ),

            border =
                BorderStroke(
                    width =
                        1.dp,

                    color =
                        AdminBorder
                )
        ) {

            Column(
                modifier =
                    Modifier.padding(
                        40.dp
                    ),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text =
                        "Nenhum cardápio encontrado",

                    fontSize = 21.sp,

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
                        "Crie um cardápio para começar a organizar suas bebidas.",

                    fontSize = 14.sp,

                    color =
                        AdminTextSecondary
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            22.dp
                        )
                )


                Button(
                    onClick =
                        onCadastrar,

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                AdminDark,

                            contentColor =
                                AdminBackground
                        )
                ) {

                    Text(
                        text =
                            "+ Criar cardápio"
                    )
                }
            }
        }
    }
}


@Composable
private fun FormularioCardapioDialog(
    cardapio: CardapioEntity?,
    onCancelar: () -> Unit,
    onSalvar: (
        nome: String,
        descricao: String
    ) -> Unit
) {

    var nome by remember(
        cardapio
    ) {

        mutableStateOf(
            cardapio?.nome ?: ""
        )
    }


    var descricao by remember(
        cardapio
    ) {

        mutableStateOf(
            cardapio?.descricao ?: ""
        )
    }


    val editando =
        cardapio != null


    AlertDialog(
        onDismissRequest =
            onCancelar,

        title = {

            Column {

                Text(
                    text =
                        if (editando) {
                            "Editar cardápio"
                        } else {
                            "Novo cardápio"
                        },

                    fontWeight =
                        FontWeight.Bold
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            4.dp
                        )
                )


                Text(
                    text =
                        if (cardapio?.fixo == true) {

                            "Você está alterando as informações do cardápio fixo."

                        } else if (editando) {

                            "Atualize as informações deste cardápio."

                        } else {

                            "Crie uma nova seleção personalizada de bebidas."
                        },

                    fontSize = 13.sp,

                    color =
                        AdminTextSecondary
                )
            }
        },

        text = {

            Column(
                verticalArrangement =
                    Arrangement.spacedBy(
                        15.dp
                    )
            ) {

                OutlinedTextField(
                    value =
                        nome,

                    onValueChange = {
                        nome = it
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {

                        Text(
                            text =
                                "Nome do cardápio"
                        )
                    },

                    placeholder = {

                        Text(
                            text =
                                "Ex.: Casamento João e Maria"
                        )
                    },

                    singleLine =
                        true
                )


                OutlinedTextField(
                    value =
                        descricao,

                    onValueChange = {
                        descricao = it
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {

                        Text(
                            text =
                                "Descrição"
                        )
                    },

                    placeholder = {

                        Text(
                            text =
                                "Ex.: Seleção especial de bebidas para o evento."
                        )
                    },

                    minLines =
                        3,

                    maxLines =
                        5
                )


                if (cardapio?.fixo == true) {

                    Card(
                        colors =
                            CardDefaults.cardColors(
                                containerColor =
                                    AdminGold.copy(
                                        alpha =
                                            0.08f
                                    )
                            ),

                        border =
                            BorderStroke(
                                width =
                                    1.dp,

                                color =
                                    AdminGold.copy(
                                        alpha =
                                            0.25f
                                    )
                            )
                    ) {

                        Text(
                            text =
                                "O cardápio fixo faz parte da estrutura padrão do aplicativo e não pode ser excluído.",

                            modifier =
                                Modifier.padding(
                                    14.dp
                                ),

                            fontSize =
                                12.sp,

                            lineHeight =
                                18.sp,

                            color =
                                AdminTextSecondary
                        )
                    }
                }
            }
        },

        confirmButton = {

            Button(
                onClick = {

                    onSalvar(
                        nome.trim(),
                        descricao.trim()
                    )
                },

                enabled =
                    nome.isNotBlank(),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            AdminDark,

                        contentColor =
                            AdminBackground
                    )
            ) {

                Text(
                    text =
                        if (editando) {
                            "Salvar alterações"
                        } else {
                            "Criar cardápio"
                        }
                )
            }
        },

        dismissButton = {

            TextButton(
                onClick =
                    onCancelar
            ) {

                Text(
                    text =
                        "Cancelar"
                )
            }
        }
    )
}
