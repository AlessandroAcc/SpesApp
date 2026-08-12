package com.example.spesapp.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.spesapp.databinding.ItemTransazioneBinding
import com.example.spesapp.model.TipoTransazione
import com.example.spesapp.model.Transazione

class AdapterTransazioni(
    private val onClick: (Transazione) -> Unit
) : ListAdapter<Transazione, AdapterTransazioni.VistaTransazione>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Transazione>() {
            override fun areItemsTheSame(oldItem: Transazione, newItem: Transazione) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Transazione, newItem: Transazione) =
                oldItem == newItem
        }
    }

    inner class VistaTransazione(
        private val binding: ItemTransazioneBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun lega(transazione: Transazione) {
            binding.textCategoria.text = transazione.categoria
            binding.textData.text = transazione.data
            if (transazione.tipo == TipoTransazione.ENTRATA) {
                binding.textImporto.text = String.format("+%.2f €", transazione.importo)
                binding.textImporto.setTextColor(Color.parseColor("#2E7D32"))
            } else {
                binding.textImporto.text = String.format("-%.2f €", transazione.importo)
                binding.textImporto.setTextColor(Color.parseColor("#C62828"))
            }
            binding.root.setOnClickListener { onClick(transazione) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VistaTransazione {
        val binding = ItemTransazioneBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VistaTransazione(binding)
    }

    override fun onBindViewHolder(holder: VistaTransazione, position: Int) {
        holder.lega(getItem(position))
    }
}