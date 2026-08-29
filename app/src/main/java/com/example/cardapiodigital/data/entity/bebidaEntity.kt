package com.example.cardapiodigital.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bebidas")
data class BebidaEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val nome: String,

    val descricao: String,

    // salva dentro do próprio tablet.
    val imagemPath: String? = null,

    val ativo: Boolean = true,

    val criadoEm: Long = System.currentTimeMillis()
)