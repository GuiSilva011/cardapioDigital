package com.example.cardapiodigital.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cardapios")
data class CardapioEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val nome: String,

    val descricao: String = "",

    // true = cardápio padrão/fixo
    // false = cardápio personalizado
    val fixo: Boolean = false,

    // Somente um cardápio ficará ativo por vez
    val ativo: Boolean = false,

    val criadoEm: Long = System.currentTimeMillis()
)