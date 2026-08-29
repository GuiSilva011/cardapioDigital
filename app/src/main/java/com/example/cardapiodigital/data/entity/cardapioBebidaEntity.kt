package com.example.cardapiodigital.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "cardapio_bebidas",

    primaryKeys = [
        "cardapioId",
        "bebidaId"
    ],

    foreignKeys = [

        ForeignKey(
            entity = CardapioEntity::class,
            parentColumns = ["id"],
            childColumns = ["cardapioId"],
            onDelete = ForeignKey.CASCADE
        ),

        ForeignKey(
            entity = BebidaEntity::class,
            parentColumns = ["id"],
            childColumns = ["bebidaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],

    indices = [
        Index("cardapioId"),
        Index("bebidaId")
    ]
)
data class CardapioBebidaEntity(

    val cardapioId: Long,

    val bebidaId: Long,

    // Controlará a ordem em que aparece no cardápio.
    val ordem: Int = 0
)