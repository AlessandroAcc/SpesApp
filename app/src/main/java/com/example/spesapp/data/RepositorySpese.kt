package com.example.spesapp.data

import android.content.Context
import com.example.spesapp.model.Transazione
import kotlinx.coroutines.flow.Flow

class RepositorySpese(context: Context) {

    private val dao = DatabaseSpesApp.ottieniIstanza(context).transazioneDao()

    fun osservaTutte(): Flow<List<Transazione>> = dao.osservaTutte()

    fun osservaPerTipo(tipo: String): Flow<List<Transazione>> = dao.osservaPerTipo(tipo)

    fun osservaPerCategoria(categoria: String): Flow<List<Transazione>> = dao.osservaPerCategoria(categoria)

    fun osservaPerMese(mese: String): Flow<List<Transazione>> = dao.osservaPerMese(mese)

    fun totaleEntrate(): Flow<Double> = dao.totaleEntrate()

    fun totaleUscite(): Flow<Double> = dao.totaleUscite()

    suspend fun inserisci(transazione: Transazione): Long = dao.inserisci(transazione)

    suspend fun aggiorna(transazione: Transazione) = dao.aggiorna(transazione)

    suspend fun eliminaPerId(id: Int) = dao.eliminaPerId(id)

    suspend fun prendiPerId(id: Int): Transazione? = dao.prendiPerId(id)
}