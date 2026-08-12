package com.example.spesapp.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spesapp.R
import com.example.spesapp.databinding.FragmentListaTransazioniBinding
import com.example.spesapp.viewmodel.TransazioneViewModel
import kotlinx.coroutines.launch

class FragmentListaTransazioni : Fragment() {

    private var _binding: FragmentListaTransazioniBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransazioneViewModel by viewModels()
    private lateinit var adapter: AdapterTransazioni
    private lateinit var adapterCategoria: ArrayAdapter<String>
    private lateinit var adapterMese: ArrayAdapter<String>
    private var categorieCorrenti: List<String> = emptyList()
    private var mesiCorrenti: List<String> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaTransazioniBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AdapterTransazioni { transazione ->
            val bundle = Bundle().apply { putInt("id", transazione.id) }
            findNavController().navigate(R.id.action_lista_to_dettaglio, bundle)
        }
        binding.recyclerTransazioni.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerTransazioni.adapter = adapter
        binding.fabAggiungi.setOnClickListener {
            findNavController().navigate(R.id.action_lista_to_form)
        }

        val adapterTipo = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("Tutti", "Entrate", "Uscite")
        )
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTipo.adapter = adapterTipo

        adapterCategoria = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            mutableListOf("Tutte")
        )
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategoria.adapter = adapterCategoria

        adapterMese = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            mutableListOf("Tutti i mesi")
        )
        adapterMese.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMese.adapter = adapterMese

        binding.spinnerTipo.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val filtro = when (position) {
                        1 -> "ENTRATA"
                        2 -> "USCITA"
                        else -> null
                    }
                    viewModel.impostaFiltroTipo(filtro)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.spinnerCategoria.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.impostaFiltroCategoria(categorieCorrenti.getOrNull(position - 1))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.spinnerMese.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.impostaFiltroMese(mesiCorrenti.getOrNull(position - 1))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    adapter.submitList(state.transazioni)
                    binding.textVuoto.visibility =
                        if (state.transazioni.isEmpty()) View.VISIBLE else View.GONE
                    binding.textTotaleEntrate.text =
                        String.format("+%.2f €", state.totaleEntrate)
                    binding.textTotaleUscite.text =
                        String.format("-%.2f €", state.totaleUscite)
                    binding.textSaldo.text = String.format("%.2f €", state.saldo)
                    if (state.saldo >= 0) {
                        binding.textSaldo.setTextColor(Color.parseColor("#2E7D32"))
                    } else {
                        binding.textSaldo.setTextColor(Color.parseColor("#C62828"))
                    }
                    if (state.categorie != categorieCorrenti) {
                        categorieCorrenti = state.categorie
                        adapterCategoria.clear()
                        adapterCategoria.add("Tutte")
                        adapterCategoria.addAll(state.categorie)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.mesi.collect { lista ->
                    if (lista != mesiCorrenti) {
                        mesiCorrenti = lista
                        adapterMese.clear()
                        adapterMese.add("Tutti i mesi")
                        adapterMese.addAll(lista)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}