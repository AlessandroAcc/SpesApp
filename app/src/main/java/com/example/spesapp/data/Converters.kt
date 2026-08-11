package com.example.spesapp.data

import androidx.room.TypeConverter
import com.example.spesapp.model.TipoTransazione

class Converters {

    @TypeConverter
    fun daTipoAString(tipo: TipoTransazione): String = tipo.name

    @TypeConverter
    fun daStringATipo(nome: String): TipoTransazione = TipoTransazione.daNome(nome)
}