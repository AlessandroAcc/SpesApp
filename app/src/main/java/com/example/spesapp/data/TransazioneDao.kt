package com.example.spesapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.spesapp.model.Transazione
import kotlinx.coroutines.flow.Flow

@Dao
interface TransazioneDao {

    @Query("SELECT * FROM transazioni ORDER BY data DESC, id DESC")
    fun osservaTutte(): Flow<List<Transazione>>

    @Query("SELECT * FROM transazioni WHERE tipo = :tipo ORDER BY data DESC")
    fun osservaPerTipo(tipo: String): Flow<List<Transazione>>

    @Query("SELECT * FROM transazioni WHERE categoria = :categoria ORDER BY data DESC")
    fun osservaPerCategoria(categoria: String): Flow<List<Transazione>>

    @Query("SELECT * FROM transazioni WHERE data LIKE :mese || '%' ORDER BY data DESC")
    fun osservaPerMese(mese: String): Flow<List<Transazione>>

    @Query("SELECT * FROM transazioni WHERE id = :id")
    suspend fun prendiPerId(id: Int): Transazione?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserisci(transazione: Transazione): Long

    @Update
    suspend fun aggiorna(transazione: Transazione)

    @Query("DELETE FROM transazioni WHERE id = :id")
    suspend fun eliminaPerId(id: Int)

    @Query("SELECT COALESCE(SUM(importo), 0) FROM transazioni WHERE tipo = 'ENTRATA'")
    fun totaleEntrate(): Flow<Double>

    @Query("SELECT COALESCE(SUM(importo), 0) FROM transazioni WHERE tipo = 'USCITA'")
    fun totaleUscite(): Flow<Double>
}