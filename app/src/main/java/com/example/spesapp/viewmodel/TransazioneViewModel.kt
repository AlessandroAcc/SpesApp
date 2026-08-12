package com.example.spesapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.spesapp.data.RepositorySpese
import com.example.spesapp.model.TipoTransazione
import com.example.spesapp.model.Transazione
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransazioneViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RepositorySpese(application)

    private val _uiState = MutableStateFlow(TransazioneUiState())
    val uiState: StateFlow<TransazioneUiState> = _uiState.asStateFlow()

    private var tutte: List<Transazione> = emptyList()

    init {
        viewModelScope.launch {
            repository.osservaTutte().collect { lista ->
                tutte = lista
                _uiState.update {
                    it.copy(categorie = lista.map { t -> t.categoria }.distinct())
                }
                aggiornaLista()
            }
        }
        viewModelScope.launch {
            repository.totaleEntrate().collect { totale ->
                _uiState.update { it.copy(totaleEntrate = totale) }
            }
        }
        viewModelScope.launch {
            repository.totaleUscite().collect { totale ->
                _uiState.update { it.copy(totaleUscite = totale) }
            }
        }
    }

    fun impostaFiltroTipo(tipo: String?) {
        _uiState.update { it.copy(filtroTipo = tipo) }
        aggiornaLista()
    }

    fun impostaFiltroCategoria(categoria: String?) {
        _uiState.update { it.copy(filtroCategoria = categoria) }
        aggiornaLista()
    }

    fun impostaFiltroMese(mese: String?) {
        _uiState.update { it.copy(filtroMese = mese) }
        aggiornaLista()
    }

    private fun aggiornaLista() {
        var lista = tutte
        _uiState.value.filtroTipo?.let { tipo ->
            lista = lista.filter { it.tipo.name == tipo }
        }
        _uiState.value.filtroCategoria?.let { categoria ->
            lista = lista.filter { it.categoria == categoria }
        }
        _uiState.value.filtroMese?.let { mese ->
            lista = lista.filter { it.data.startsWith(mese) }
        }
        _uiState.update { it.copy(transazioni = lista) }
    }

    fun salvaTransazione(
        importo: Double,
        tipo: TipoTransazione,
        categoria: String,
        data: String,
        nota: String?
    ) {
        viewModelScope.launch {
            repository.inserisci(
                Transazione(
                    importo = importo,
                    tipo = tipo,
                    categoria = categoria,
                    data = data,
                    nota = nota
                )
            )
        }
    }

    fun aggiornaTransazione(transazione: Transazione) {
        viewModelScope.launch {
            repository.aggiorna(transazione)
        }
    }

    fun eliminaTransazione(id: Int) {
        viewModelScope.launch {
            repository.eliminaPerId(id)
        }
    }

    suspend fun prendiTransazione(id: Int): Transazione? {
        return repository.prendiPerId(id)
    }
}