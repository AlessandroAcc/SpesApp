package com.example.spesapp

import com.example.spesapp.model.TipoTransazione
import com.example.spesapp.model.Transazione
import org.junit.Assert.assertEquals
import org.junit.Test

class TransazioneTest {

    @Test
    fun `crea transazione con valori giusti`() {
        val t = Transazione(
            importo = 12.50,
            tipo = TipoTransazione.USCITA,
            categoria = "Cibo",
            data = "2026-08-12",
            nota = "pizza"
        )
        assertEquals(12.50, t.importo, 0.001)
        assertEquals(TipoTransazione.USCITA, t.tipo)
        assertEquals("Cibo", t.categoria)
        assertEquals("pizza", t.nota)
    }

    @Test
    fun `filtro per mese prende solo quel mese`() {
        val lista = listOf(
            Transazione(
                importo = 10.0,
                tipo = TipoTransazione.USCITA,
                categoria = "Cibo",
                data = "2026-08-01",
                nota = null
            ),
            Transazione(
                importo = 20.0,
                tipo = TipoTransazione.USCITA,
                categoria = "Svago",
                data = "2026-07-15",
                nota = null
            )
        )
        val filtrate = lista.filter { it.data.startsWith("2026-08") }
        assertEquals(1, filtrate.size)
        assertEquals("Cibo", filtrate[0].categoria)
    }

    @Test
    fun `totale uscite sommato giusto`() {
        val lista = listOf(
            Transazione(
                importo = 10.0,
                tipo = TipoTransazione.USCITA,
                categoria = "Cibo",
                data = "2026-08-01",
                nota = null
            ),
            Transazione(
                importo = 20.0,
                tipo = TipoTransazione.USCITA,
                categoria = "Svago",
                data = "2026-08-02",
                nota = null
            ),
            Transazione(
                importo = 100.0,
                tipo = TipoTransazione.ENTRATA,
                categoria = "Stipendio",
                data = "2026-08-03",
                nota = null
            )
        )
        val totaleUscite = lista
            .filter { it.tipo == TipoTransazione.USCITA }
            .sumOf { it.importo }
        assertEquals(30.0, totaleUscite, 0.001)
    }
}