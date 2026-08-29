package com.example.cardapiodigital.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.cardapiodigital.data.entity.BebidaEntity
import com.example.cardapiodigital.data.entity.CardapioBebidaEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CardapioBebidaDao {

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    abstract suspend fun adicionar(
        relacao: CardapioBebidaEntity
    )

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    abstract suspend fun adicionarVarias(
        relacoes: List<CardapioBebidaEntity>
    )

    @Query(
        """
        DELETE FROM cardapio_bebidas
        WHERE cardapioId = :cardapioId
        AND bebidaId = :bebidaId
        """
    )
    abstract suspend fun remover(
        cardapioId: Long,
        bebidaId: Long
    )

    @Query(
        """
        DELETE FROM cardapio_bebidas
        WHERE cardapioId = :cardapioId
        """
    )
    abstract suspend fun removerTodasDoCardapio(
        cardapioId: Long
    )

    @Query(
        """
        SELECT bebidaId
        FROM cardapio_bebidas
        WHERE cardapioId = :cardapioId
        ORDER BY ordem ASC
        """
    )
    abstract suspend fun listarIdsBebidas(
        cardapioId: Long
    ): List<Long>

    @Query(
        """
        SELECT bebidas.*
        FROM bebidas
        INNER JOIN cardapio_bebidas
            ON bebidas.id = cardapio_bebidas.bebidaId
        WHERE cardapio_bebidas.cardapioId = :cardapioId
        AND bebidas.ativo = 1
        ORDER BY cardapio_bebidas.ordem ASC
        """
    )
    abstract fun listarBebidasDoCardapio(
        cardapioId: Long
    ): Flow<List<BebidaEntity>>

    @Transaction
    open suspend fun substituirBebidasDoCardapio(
        cardapioId: Long,
        bebidaIds: List<Long>
    ) {
        removerTodasDoCardapio(cardapioId)

        if (bebidaIds.isEmpty()) {
            return
        }

        val relacoes = bebidaIds.mapIndexed { index, bebidaId ->

            CardapioBebidaEntity(
                cardapioId = cardapioId,
                bebidaId = bebidaId,
                ordem = index
            )
        }

        adicionarVarias(relacoes)
    }
}