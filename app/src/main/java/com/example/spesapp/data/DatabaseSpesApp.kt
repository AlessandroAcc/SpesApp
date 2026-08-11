package com.example.spesapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.spesapp.model.Transazione

@Database(entities = [Transazione::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DatabaseSpesApp : RoomDatabase() {

    abstract fun transazioneDao(): TransazioneDao

    companion object {
        @Volatile
        private var istanza: DatabaseSpesApp? = null

        fun ottieniIstanza(context: Context): DatabaseSpesApp {
            return istanza ?: synchronized(this) {
                val nuova = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseSpesApp::class.java,
                    "spesapp.db"
                ).build()
                istanza = nuova
                nuova
            }
        }
    }
}