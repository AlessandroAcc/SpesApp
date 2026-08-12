package com.example.spesapp.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.spesapp.databinding.FragmentGraficoBinding
import com.example.spesapp.model.TipoTransazione
import com.example.spesapp.viewmodel.TransazioneViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.launch

class FragmentGrafico : Fragment() {

    private var _binding: FragmentGraficoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransazioneViewModel by viewModels()

    private val colori = listOf(
        "#EF5350", "#42A5F5", "#FFA726", "#66BB6A",
        "#AB47BC", "#26C6DA", "#FFCA28", "#8D6E63"
    ).map { Color.parseColor(it) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGraficoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    val uscite = state.transazioni.filter { it.tipo == TipoTransazione.USCITA }
                    if (uscite.isEmpty()) {
                        binding.pieChart.visibility = View.GONE
                        binding.textNessunaUscita.visibility = View.VISIBLE
                    } else {
                        binding.pieChart.visibility = View.VISIBLE
                        binding.textNessunaUscita.visibility = View.GONE
                        val totali = uscite
                            .groupBy { it.categoria }
                            .mapValues { voce -> voce.value.sumOf { it.importo } }
                        val voci = totali.map { (categoria, totale) ->
                            PieEntry(totale.toFloat(), categoria)
                        }
                        val dataSet = PieDataSet(voci, "Uscite per categoria")
                        dataSet.colors = colori
                        dataSet.valueTextColor = Color.WHITE
                        dataSet.valueTextSize = 14f
                        binding.pieChart.data = PieData(dataSet)
                        binding.pieChart.description.isEnabled = false
                        binding.pieChart.invalidate()
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