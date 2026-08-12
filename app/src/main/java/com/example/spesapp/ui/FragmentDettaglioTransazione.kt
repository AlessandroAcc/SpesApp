package com.example.spesapp.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spesapp.databinding.FragmentDettaglioTransazioneBinding
import com.example.spesapp.viewmodel.TransazioneViewModel
import kotlinx.coroutines.launch
import com.example.spesapp.R

class FragmentDettaglioTransazione : Fragment() {

    private var _binding: FragmentDettaglioTransazioneBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransazioneViewModel by viewModels()
    private var idTransazione: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDettaglioTransazioneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idTransazione = arguments?.getInt("id", -1) ?: -1
        if (idTransazione == -1) {
            findNavController().popBackStack()
            return
        }
        viewLifecycleOwner.lifecycleScope.launch {
            val transazione = viewModel.prendiTransazione(idTransazione)
            if (transazione == null) {
                findNavController().popBackStack()
            } else {
                binding.textImportoDettaglio.text = String.format("%.2f €", transazione.importo)
                binding.textTipoDettaglio.text = transazione.tipo.name
                binding.textCategoriaDettaglio.text = transazione.categoria
                binding.textDataDettaglio.text = transazione.data
                binding.textNotaDettaglio.text = transazione.nota ?: "Nessuna nota"
                if (transazione.tipo.name == "ENTRATA") {
                    binding.textImportoDettaglio.setTextColor(Color.parseColor("#2E7D32"))
                } else {
                    binding.textImportoDettaglio.setTextColor(Color.parseColor("#C62828"))
                }
            }
        }
        binding.btnModifica.setOnClickListener {
            val bundle = Bundle().apply { putInt("id", idTransazione) }
            findNavController().navigate(R.id.action_dettaglio_to_form, bundle)
        }
        binding.btnElimina.setOnClickListener {
            viewModel.eliminaTransazione(idTransazione)
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}