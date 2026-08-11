package com.example.spesapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transazioni")
data class Transazione(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "importo")
    val importo: Double,

    @ColumnInfo(name = "tipo")
    val tipo: TipoTransazione,

    @ColumnInfo(name = "categoria")
    val categoria: String,

    @ColumnInfo(name = "data")
    val data: String,

    @ColumnInfo(name = "nota")
    val nota: String? = null
) {
    val importoConSegno: Double
        get() = if (tipo == TipoTransazione.ENTRATA) importo else -importo
}