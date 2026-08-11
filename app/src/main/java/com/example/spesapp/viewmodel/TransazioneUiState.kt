package com.example.spesapp.viewmodel

import com.example.spesapp.model.Transazione

data class TransazioneUiState(
    val transazioni: List<Transazione> = emptyList(),
    val totaleEntrate: Double = 0.0,
    val totaleUscite: Double = 0.0,
    val categorie: List<String> = emptyList(),
    val filtroTipo: String? = null,
    val filtroCategoria: String? = null,
    val filtroMese: String? = null
) {
    val saldo: Double
        get() = totaleEntrate - totaleUscite
}