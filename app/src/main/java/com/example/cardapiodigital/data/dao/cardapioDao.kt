package com.example.cardapiodigital.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.cardapiodigital.data.entity.CardapioEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CardapioDao {

    @Insert
    abstract suspend fun inserir(
        cardapio: CardapioEntity
    ): Long

    @Update
    abstract suspend fun atualizar(
        cardapio: CardapioEntity
    )

    @Query(
        """
        SELECT *
        FROM cardapios
        ORDER BY fixo DESC, criadoEm DESC
        """
    )
    abstract fun listarTodos(): Flow<List<CardapioEntity>>

    @Query(
        """
        SELECT *
        FROM cardapios
        WHERE id = :id
        LIMIT 1
        """
    )
    abstract suspend fun buscarPorId(
        id: Long
    ): CardapioEntity?

    @Query(
        """
        SELECT *
        FROM cardapios
        WHERE fixo = 1
        LIMIT 1
        """
    )
    abstract suspend fun buscarCardapioFixo(): CardapioEntity?

    @Query(
        """
        SELECT *
        FROM cardapios
        WHERE ativo = 1
        LIMIT 1
        """
    )
    abstract fun observarCardapioAtivo(): Flow<CardapioEntity?>

    @Query(
        """
        UPDATE cardapios
        SET ativo = 0
        """
    )
    protected abstract suspend fun desativarTodos()

    @Query(
        """
        UPDATE cardapios
        SET ativo = 1
        WHERE id = :id
        """
    )
    protected abstract suspend fun ativarPorId(
        id: Long
    )

    @Transaction
    open suspend fun ativarCardapio(
        id: Long
    ) {
        desativarTodos()
        ativarPorId(id)
    }

    @Query(
        """
        DELETE FROM cardapios
        WHERE id = :id
        AND fixo = 0
        """
    )
    abstract suspend fun excluirPersonalizado(
        id: Long
    )
}