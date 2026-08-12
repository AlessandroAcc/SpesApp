package com.example.spesapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.spesapp.databinding.FragmentFormTransazioneBinding
import com.example.spesapp.model.TipoTransazione
import com.example.spesapp.viewmodel.TransazioneViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FragmentFormTransazione : Fragment() {

    private var _binding: FragmentFormTransazioneBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransazioneViewModel by viewModels()
    private val formatoData = SimpleDateFormat("yyyy-MM-dd", Locale.ITALY)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormTransazioneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editData.setText(formatoData.format(Date()))
        binding.btnSalva.setOnClickListener { salva() }
    }

    private fun salva() {
        val testoImporto = binding.editImporto.text.toString()
        val categoria = binding.editCategoria.text.toString().trim()
        val data = binding.editData.text.toString().trim()
        val nota = binding.editNota.text.toString().trim()

        if (testoImporto.isEmpty()) {
            binding.editImporto.error = "Inserisci un importo"
            return
        }
        val importo = testoImporto.toDoubleOrNull()
        if (importo == null || importo <= 0) {
            binding.editImporto.error = "Importo non valido"
            return
        }
        if (categoria.isEmpty()) {
            binding.editCategoria.error = "Inserisci una categoria"
            return
        }
        val dataValida = try {
            formatoData.isLenient = false
            formatoData.parse(data) != null
        } catch (e: Exception) {
            false
        }
        if (!dataValida) {
            binding.editData.error = "Data non valida, usa AAAA-MM-GG"
            return
        }

        val tipo = if (binding.radioEntrata.isChecked) {
            TipoTransazione.ENTRATA
        } else {
            TipoTransazione.USCITA
        }

        viewModel.salvaTransazione(
            importo = importo,
            tipo = tipo,
            categoria = categoria,
            data = data,
            nota = nota.ifEmpty { null }
        )
        Toast.makeText(requireContext(), "Transazione salvata", Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}