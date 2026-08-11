package com.example.spesapp.model

enum class TipoTransazione(val etichetta: String) {
    ENTRATA("Entrata"),
    USCITA("Uscita");

    companion object {
        fun daNome(nome: String): TipoTransazione =
            entries.firstOrNull { it.name == nome } ?: USCITA
    }
}