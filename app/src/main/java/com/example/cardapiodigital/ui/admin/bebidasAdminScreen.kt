package com.example.cardapiodigital.ui.admin

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import com.example.cardapiodigital.viewmodel.BebidaViewModel
import java.io.File


/*
 * =========================================================
 * PALETA ADMIN
 * =========================================================
 */

private val AdminBackground =
    Color(0xFFF5F5F3)

private val AdminSurface =
    Color(0xFFFFFFFF)

private val AdminBorder =
    Color(0xFFE2E2DE)

private val AdminText =
    Color(0xFF171717)

private val AdminTextSecondary =
    Color(0xFF6B6B67)

private val AdminDark =
    Color(0xFF181818)

private val AdminGold =
    Color(0xFFB08A48)

private val AdminSoft =
    Color(0xFFF0F0EC)

private val AdminGreen =
    Color(0xFF3E7652)

private val AdminRed =
    Color(0xFFB14646)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BebidasAdminScreen(
    viewModel: BebidaViewModel,
    onVoltar: () -> Unit
) {

    val bebidas by
    viewModel.bebidas.collectAsStateWithLifecycle()

    var busca by remember {
        mutableStateOf("")
    }

    var mostrarFormulario by remember {
        mutableStateOf(false)
    }

    var bebidaEmEdicao by remember {
        mutableStateOf<BebidaEntity?>(null)
    }

    var bebidaParaExcluir by remember {
        mutableStateOf<BebidaEntity?>(null)
    }


    /*
     * FILTRO DA BUSCA
     */
    val bebidasFiltradas =
        bebidas.filter { bebida ->

            busca.isBlank() ||
                    bebida.nome.contains(
                        busca,
                        ignoreCase = true
                    ) ||
                    bebida.descricao.contains(
                        busca,
                        ignoreCase = true
                    )
        }


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

                title = {

                    Text(
                        text =
                            "Gerenciamento de bebidas",

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
                            "CATÁLOGO",

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
                            "Bebidas",

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
                            "Cadastre e mantenha o catálogo utilizado nos cardápios.",

                        fontSize = 15.sp,

                        color =
                            AdminTextSecondary
                    )
                }


                Button(
                    onClick = {

                        bebidaEmEdicao =
                            null

                        mostrarFormulario =
                            true
                    },

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                AdminDark,

                            contentColor =
                                Color.White
                        ),

                    shape =
                        RoundedCornerShape(
                            11.dp
                        )
                ) {

                    Text(
                        text =
                            "+ Nova bebida",

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

                ResumoBebidasCard(
                    titulo =
                        "Total",

                    valor =
                        bebidas.size.toString(),

                    modifier =
                        Modifier.weight(1f)
                )


                ResumoBebidasCard(
                    titulo =
                        "Ativas",

                    valor =
                        quantidadeAtivas.toString(),

                    modifier =
                        Modifier.weight(1f)
                )


                ResumoBebidasCard(
                    titulo =
                        "Inativas",

                    valor =
                        (bebidas.size - quantidadeAtivas)
                            .toString(),

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
             * PESQUISA
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
                        22.dp
                    )
            )


            HorizontalDivider(
                color =
                    AdminBorder
            )


            Spacer(
                modifier =
                    Modifier.height(
                        18.dp
                    )
            )


            /*
             * =================================================
             * CONTEÚDO
             * =================================================
             */

            when {

                bebidas.isEmpty() -> {

                    EstadoVazioBebidas(
                        titulo =
                            "Nenhuma bebida cadastrada",

                        descricao =
                            "Cadastre a primeira bebida para começar a montar seus cardápios.",

                        onCadastrar = {

                            bebidaEmEdicao =
                                null

                            mostrarFormulario =
                                true
                        }
                    )
                }


                bebidasFiltradas.isEmpty() -> {

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
                                    "Nenhum resultado encontrado",

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
                                bebidasFiltradas,

                            key = {
                                    bebida ->
                                bebida.id
                            }
                        ) { bebida ->

                            BebidaAdminCard(
                                bebida =
                                    bebida,

                                onEditar = {

                                    bebidaEmEdicao =
                                        bebida

                                    mostrarFormulario =
                                        true
                                },

                                onExcluir = {

                                    bebidaParaExcluir =
                                        bebida
                                },

                                onAlterarStatus = {
                                        novoStatus ->

                                    viewModel
                                        .alterarStatus(
                                            bebida =
                                                bebida,

                                            ativo =
                                                novoStatus
                                        )
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

        FormularioBebidaDialog(
            bebida =
                bebidaEmEdicao,

            onCancelar = {

                mostrarFormulario =
                    false

                bebidaEmEdicao =
                    null
            },

            onSalvar = {
                    nome,
                    descricao,
                    imagemUri ->

                val bebidaAtual =
                    bebidaEmEdicao


                if (bebidaAtual == null) {

                    viewModel
                        .cadastrarBebida(
                            nome =
                                nome,

                            descricao =
                                descricao,

                            imagemUri =
                                imagemUri
                        )

                } else {

                    viewModel
                        .editarBebida(
                            bebida =
                                bebidaAtual,

                            nome =
                                nome,

                            descricao =
                                descricao,

                            novaImagemUri =
                                imagemUri
                        )
                }


                mostrarFormulario =
                    false

                bebidaEmEdicao =
                    null
            }
        )
    }


    /*
     * =====================================================
     * CONFIRMAÇÃO DE EXCLUSÃO
     * =====================================================
     */

    bebidaParaExcluir?.let {
            bebida ->

        AlertDialog(
            onDismissRequest = {

                bebidaParaExcluir =
                    null
            },

            title = {

                Text(
                    text =
                        "Excluir bebida"
                )
            },

            text = {

                Text(
                    text =
                        "Deseja realmente excluir \"${bebida.nome}\"?\n\nA bebida também será removida dos cardápios em que estiver vinculada."
                )
            },

            confirmButton = {

                Button(
                    onClick = {

                        viewModel
                            .excluirBebida(
                                bebida
                            )

                        bebidaParaExcluir =
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

                        bebidaParaExcluir =
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
private fun ResumoBebidasCard(
    titulo: String,
    valor: String,
    modifier: Modifier = Modifier
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

        Column(
            modifier =
                Modifier.padding(
                    18.dp
                )
        ) {

            Text(
                text =
                    titulo.uppercase(),

                fontSize = 10.sp,

                letterSpacing =
                    1.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    AdminTextSecondary
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

                fontSize = 25.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    AdminText
            )
        }
    }
}


@Composable
private fun BebidaAdminCard(
    bebida: BebidaEntity,
    onEditar: () -> Unit,
    onExcluir: () -> Unit,
    onAlterarStatus: (Boolean) -> Unit
) {

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
                    18.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            /*
             * FOTO
             */
            Box(
                modifier = Modifier
                    .size(
                        104.dp
                    )
                    .clip(
                        RoundedCornerShape(
                            13.dp
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

                        SemImagemAdmin()
                    }

                } else {

                    SemImagemAdmin()
                }
            }


            Spacer(
                modifier =
                    Modifier.width(
                        20.dp
                    )
            )


            /*
             * INFORMAÇÕES
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

                        fontSize = 19.sp,

                        fontWeight =
                            FontWeight.SemiBold,

                        color =
                            AdminText,

                        maxLines = 1,

                        overflow =
                            TextOverflow.Ellipsis
                    )


                    Spacer(
                        modifier =
                            Modifier.width(
                                10.dp
                            )
                    )


                    StatusBebida(
                        ativo =
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
                                7.dp
                            )
                    )


                    Text(
                        text =
                            bebida.descricao,

                        fontSize = 14.sp,

                        lineHeight =
                            20.sp,

                        color =
                            AdminTextSecondary,

                        maxLines = 2,

                        overflow =
                            TextOverflow.Ellipsis
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
             * STATUS
             */
            Column(
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text =
                        if (
                            bebida.ativo
                        ) {
                            "Exibir"
                        } else {
                            "Oculta"
                        },

                    fontSize = 11.sp,

                    color =
                        AdminTextSecondary
                )


                Switch(
                    checked =
                        bebida.ativo,

                    onCheckedChange =
                        onAlterarStatus
                )
            }


            Spacer(
                modifier =
                    Modifier.width(
                        14.dp
                    )
            )


            /*
             * AÇÕES
             */
            OutlinedButton(
                onClick =
                    onEditar,

                shape =
                    RoundedCornerShape(
                        10.dp
                    ),

                border =
                    BorderStroke(
                        1.dp,
                        AdminBorder
                    )
            ) {

                Text(
                    text =
                        "Editar",

                    color =
                        AdminText
                )
            }


            Spacer(
                modifier =
                    Modifier.width(
                        6.dp
                    )
            )


            TextButton(
                onClick =
                    onExcluir
            ) {

                Text(
                    text =
                        "Excluir",

                    color =
                        AdminRed
                )
            }
        }
    }
}


@Composable
private fun StatusBebida(
    ativo: Boolean
) {

    Box(
        modifier = Modifier
            .background(
                color =
                    if (ativo) {

                        AdminGreen.copy(
                            alpha =
                                0.10f
                        )

                    } else {

                        AdminTextSecondary.copy(
                            alpha =
                                0.10f
                        )
                    },

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
                if (ativo) {
                    "ATIVA"
                } else {
                    "INATIVA"
                },

            fontSize = 9.sp,

            letterSpacing =
                0.7.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                if (ativo) {
                    AdminGreen
                } else {
                    AdminTextSecondary
                }
        )
    }
}


@Composable
private fun SemImagemAdmin() {

    Text(
        text =
            "Sem foto",

        fontSize =
            11.sp,

        color =
            AdminTextSecondary
    )
}


@Composable
private fun EstadoVazioBebidas(
    titulo: String,
    descricao: String,
    onCadastrar: () -> Unit
) {

    Box(
        modifier =
            Modifier.fillMaxSize(),

        contentAlignment =
            Alignment.Center
    ) {

        Card(
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
                        titulo,

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
                        descricao,

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
                                Color.White
                        )
                ) {

                    Text(
                        text =
                            "+ Cadastrar bebida"
                    )
                }
            }
        }
    }
}


@Composable
private fun FormularioBebidaDialog(
    bebida: BebidaEntity?,
    onCancelar: () -> Unit,
    onSalvar: (
        nome: String,
        descricao: String,
        imagemUri: Uri?
    ) -> Unit
) {

    var nome by remember(
        bebida
    ) {

        mutableStateOf(
            bebida?.nome ?: ""
        )
    }


    var descricao by remember(
        bebida
    ) {

        mutableStateOf(
            bebida?.descricao ?: ""
        )
    }


    /*
     * Só representa uma NOVA imagem
     * escolhida nesta edição.
     */
    var imagemSelecionada by remember(
        bebida
    ) {

        mutableStateOf<Uri?>(
            null
        )
    }


    val seletorImagem =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts
                    .PickVisualMedia()
        ) { uri ->

            if (uri != null) {

                imagemSelecionada =
                    uri
            }
        }


    val editando =
        bebida != null


    AlertDialog(
        onDismissRequest =
            onCancelar,

        title = {

            Column {

                Text(
                    text =
                        if (editando) {
                            "Editar bebida"
                        } else {
                            "Nova bebida"
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
                        if (editando) {
                            "Atualize os dados da bebida."
                        } else {
                            "Cadastre uma nova bebida no catálogo."
                        },

                    fontSize = 13.sp,

                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant
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

                /*
                 * FOTO
                 */
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            210.dp
                        )
                        .clip(
                            RoundedCornerShape(
                                14.dp
                            )
                        )
                        .background(
                            AdminSoft
                        ),

                    contentAlignment =
                        Alignment.Center
                ) {

                    val imagemModel =
                        imagemSelecionada
                            ?: bebida
                                ?.imagemPath
                                ?.let {
                                        caminho ->

                                    File(
                                        caminho
                                    )
                                }


                    if (
                        imagemModel != null
                    ) {

                        AsyncImage(
                            model =
                                imagemModel,

                            contentDescription =
                                "Foto da bebida",

                            modifier =
                                Modifier.fillMaxSize(),

                            contentScale =
                                ContentScale.Crop
                        )

                    } else {

                        Column(
                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {

                            Text(
                                text =
                                    "Nenhuma foto selecionada",

                                fontSize = 13.sp,

                                color =
                                    AdminTextSecondary
                            )


                            Spacer(
                                modifier =
                                    Modifier.height(
                                        3.dp
                                    )
                            )


                            Text(
                                text =
                                    "A imagem será exibida no cardápio.",

                                fontSize = 11.sp,

                                color =
                                    AdminTextSecondary
                            )
                        }
                    }
                }


                OutlinedButton(
                    onClick = {

                        seletorImagem.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts
                                    .PickVisualMedia
                                    .ImageOnly
                            )
                        )
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Text(
                        text =
                            if (
                                imagemSelecionada != null ||
                                !bebida
                                    ?.imagemPath
                                    .isNullOrBlank()
                            ) {

                                "Trocar foto"

                            } else {

                                "Selecionar foto"
                            }
                    )
                }


                /*
                 * NOME
                 */
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
                                "Nome da bebida"
                        )
                    },

                    singleLine =
                        true
                )


                /*
                 * DESCRIÇÃO
                 */
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
                                "Ex.: Gin, água tônica e limão siciliano."
                        )
                    },

                    minLines =
                        3,

                    maxLines =
                        5
                )
            }
        },

        confirmButton = {

            Button(
                onClick = {

                    onSalvar(
                        nome,
                        descricao,
                        imagemSelecionada
                    )
                },

                enabled =
                    nome.isNotBlank(),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            AdminDark,

                        contentColor =
                            Color.White
                    )
            ) {

                Text(
                    text =
                        if (editando) {
                            "Salvar alterações"
                        } else {
                            "Cadastrar bebida"
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