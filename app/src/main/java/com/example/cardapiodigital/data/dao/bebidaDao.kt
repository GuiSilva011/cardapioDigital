package com.example.cardapiodigital.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.cardapiodigital.data.entity.BebidaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BebidaDao {

    @Insert
    suspend fun inserir(bebida: BebidaEntity): Long

    @Update
    suspend fun atualizar(bebida: BebidaEntity)

    @Delete
    suspend fun excluir(bebida: BebidaEntity)

    @Query(
        """
        SELECT *
        FROM bebidas
        ORDER BY nome ASC
        """
    )
    fun listarTodas(): Flow<List<BebidaEntity>>

    @Query(
        """
        SELECT *
        FROM bebidas
        WHERE ativo = 1
        ORDER BY nome ASC
        """
    )
    fun listarAtivas(): Flow<List<BebidaEntity>>

    @Query(
        """
        SELECT *
        FROM bebidas
        WHERE id = :id
        LIMIT 1
        """
    )
    suspend fun buscarPorId(id: Long): BebidaEntity?

    @Query(
        """
        UPDATE bebidas
        SET ativo = :ativo
        WHERE id = :id
        """
    )
    suspend fun alterarStatus(
        id: Long,
        ativo: Boolean
    )
}