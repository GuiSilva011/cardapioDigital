package com.example.cardapiodigital.data.security

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PinBackground =
    Color(0xFF111111)

private val PinGold =
    Color(0xFFC6A15B)

private val PinWhite =
    Color(0xFFFFFFFF)

private val PinBorder =
    Color(0xFF4A402F)

private val PinError =
    Color(0xFFFF8A80)

@Composable
fun AdminAccessDialog(
    pinConfigurado: Boolean,
    onDismiss: () -> Unit,
    onValidarPin: (String) -> Boolean,
    onCriarPin: (String) -> Unit,
    onAcessoLiberado: () -> Unit
) {
    var pin by
    rememberSaveable {
        mutableStateOf("")
    }

    var confirmacaoPin by
    rememberSaveable {
        mutableStateOf("")
    }

    var mensagemErro by
    rememberSaveable {
        mutableStateOf<String?>(null)
    }

    val confirmar = {
        mensagemErro = null

        if (!AdminPinStore.pinValido(pin)) {
            mensagemErro =
                "Digite um PIN com ${AdminPinStore.PIN_LENGTH} números."
        } else if (!pinConfigurado && pin != confirmacaoPin) {
            mensagemErro =
                "Os dois PINs não são iguais."
        } else if (pinConfigurado && !onValidarPin(pin)) {
            pin = ""
            mensagemErro =
                "PIN incorreto. Tente novamente."
        } else {
            if (!pinConfigurado) {
                onCriarPin(pin)
            }

            onAcessoLiberado()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,

        containerColor =
            PinBackground,

        titleContentColor =
            PinGold,

        textContentColor =
            PinWhite,

        title = {
            Text(
                text =
                    if (pinConfigurado) {
                        "Acesso administrativo"
                    } else {
                        "Criar PIN administrativo"
                    },

                fontSize = 24.sp,

                fontWeight =
                    FontWeight.Bold
            )
        },

        text = {
            Column(
                verticalArrangement =
                    Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text =
                        if (pinConfigurado) {
                            "Digite o PIN para abrir o painel."
                        } else {
                            "No primeiro acesso, crie um PIN de ${AdminPinStore.PIN_LENGTH} números. Ele ficará salvo somente neste tablet."
                        },

                    color =
                        PinWhite,

                    lineHeight =
                        21.sp
                )

                PinField(
                    value = pin,
                    onValueChange = {
                        pin = it
                        mensagemErro = null
                    },
                    label =
                        if (pinConfigurado) {
                            "PIN"
                        } else {
                            "Novo PIN"
                        },
                    imeAction =
                        if (pinConfigurado) {
                            ImeAction.Done
                        } else {
                            ImeAction.Next
                        },
                    onDone = confirmar
                )

                if (!pinConfigurado) {
                    PinField(
                        value =
                            confirmacaoPin,
                        onValueChange = {
                            confirmacaoPin = it
                            mensagemErro = null
                        },
                        label =
                            "Confirmar PIN",
                        imeAction =
                            ImeAction.Done,
                        onDone =
                            confirmar
                    )
                }

                mensagemErro?.let { mensagem ->
                    Text(
                        text = mensagem,
                        color = PinError,
                        fontSize = 13.sp
                    )
                }
            }
        },

        confirmButton = {
            Button(
                onClick = confirmar,

                enabled =
                    if (pinConfigurado) {
                        pin.length == AdminPinStore.PIN_LENGTH
                    } else {
                        pin.length == AdminPinStore.PIN_LENGTH &&
                                confirmacaoPin.length == AdminPinStore.PIN_LENGTH
                    },

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            PinGold,
                        contentColor =
                            Color.Black,
                        disabledContainerColor =
                            PinGold.copy(alpha = 0.30f),
                        disabledContentColor =
                            Color.Black.copy(alpha = 0.55f)
                    )
            ) {
                Text(
                    text =
                        if (pinConfigurado) {
                            "Entrar"
                        } else {
                            "Criar e entrar"
                        },

                    fontWeight =
                        FontWeight.SemiBold
                )
            }
        },

        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "Cancelar",
                    color = PinWhite
                )
            }
        }
    )
}

@Composable
fun AlterarAdminPinDialog(
    onDismiss: () -> Unit,
    onAlterarPin: (
        pinAtual: String,
        novoPin: String
    ) -> Boolean,
    onPinAlterado: () -> Unit
) {
    var pinAtual by
    rememberSaveable {
        mutableStateOf("")
    }

    var novoPin by
    rememberSaveable {
        mutableStateOf("")
    }

    var confirmacaoPin by
    rememberSaveable {
        mutableStateOf("")
    }

    var mensagemErro by
    rememberSaveable {
        mutableStateOf<String?>(null)
    }

    val confirmar = {
        mensagemErro = null

        when {
            !AdminPinStore.pinValido(pinAtual) ||
                    !AdminPinStore.pinValido(novoPin) -> {
                mensagemErro =
                    "Todos os PINs devem ter ${AdminPinStore.PIN_LENGTH} números."
            }

            novoPin != confirmacaoPin -> {
                mensagemErro =
                    "A confirmação não corresponde ao novo PIN."
            }

            novoPin == pinAtual -> {
                mensagemErro =
                    "Escolha um PIN diferente do atual."
            }

            !onAlterarPin(
                pinAtual,
                novoPin
            ) -> {
                pinAtual = ""
                mensagemErro =
                    "O PIN atual está incorreto."
            }

            else -> {
                onPinAlterado()
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,

        containerColor =
            PinBackground,

        titleContentColor =
            PinGold,

        textContentColor =
            PinWhite,

        title = {
            Text(
                text = "Alterar PIN",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        },

        text = {
            Column(
                verticalArrangement =
                    Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text =
                        "Confirme o PIN atual antes de cadastrar um novo.",

                    color = PinWhite
                )

                PinField(
                    value = pinAtual,
                    onValueChange = {
                        pinAtual = it
                        mensagemErro = null
                    },
                    label = "PIN atual",
                    imeAction = ImeAction.Next
                )

                PinField(
                    value = novoPin,
                    onValueChange = {
                        novoPin = it
                        mensagemErro = null
                    },
                    label = "Novo PIN",
                    imeAction = ImeAction.Next
                )

                PinField(
                    value = confirmacaoPin,
                    onValueChange = {
                        confirmacaoPin = it
                        mensagemErro = null
                    },
                    label = "Confirmar novo PIN",
                    imeAction = ImeAction.Done,
                    onDone = confirmar
                )

                mensagemErro?.let { mensagem ->
                    Text(
                        text = mensagem,
                        color = PinError,
                        fontSize = 13.sp
                    )
                }
            }
        },

        confirmButton = {
            Button(
                onClick = confirmar,

                enabled =
                    pinAtual.length == AdminPinStore.PIN_LENGTH &&
                            novoPin.length == AdminPinStore.PIN_LENGTH &&
                            confirmacaoPin.length == AdminPinStore.PIN_LENGTH,

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = PinGold,
                        contentColor = Color.Black,
                        disabledContainerColor =
                            PinGold.copy(alpha = 0.30f),
                        disabledContentColor =
                            Color.Black.copy(alpha = 0.55f)
                    )
            ) {
                Text(
                    text = "Salvar novo PIN",
                    fontWeight = FontWeight.SemiBold
                )
            }
        },

        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "Cancelar",
                    color = PinWhite
                )
            }
        }
    )
}

@Composable
private fun PinField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    imeAction: ImeAction,
    onDone: () -> Unit = {}
) {
    OutlinedTextField(
        value = value,

        onValueChange = { novoValor ->
            if (
                novoValor.length <= AdminPinStore.PIN_LENGTH &&
                novoValor.all { caractere ->
                    caractere.isDigit()
                }
            ) {
                onValueChange(novoValor)
            }
        },

        modifier =
            Modifier.fillMaxWidth(),

        label = {
            Text(label)
        },

        singleLine = true,

        visualTransformation =
            PasswordVisualTransformation(),

        keyboardOptions =
            KeyboardOptions(
                keyboardType =
                    KeyboardType.NumberPassword,
                imeAction =
                    imeAction
            ),

        keyboardActions =
            KeyboardActions(
                onDone = {
                    onDone()
                }
            ),

        colors =
            OutlinedTextFieldDefaults.colors(
                focusedTextColor = PinWhite,
                unfocusedTextColor = PinWhite,
                cursorColor = PinGold,
                focusedBorderColor = PinGold,
                unfocusedBorderColor = PinBorder,
                focusedLabelColor = PinGold,
                unfocusedLabelColor =
                    PinWhite.copy(alpha = 0.70f)
            )
    )

    Spacer(
        modifier =
            Modifier.height(1.dp)
    )
}
