package com.example.spesapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spesapp.databinding.FragmentListaTransazioniBinding
import com.example.spesapp.viewmodel.TransazioneViewModel
import kotlinx.coroutines.launch

class FragmentListaTransazioni : Fragment() {

    private var _binding: FragmentListaTransazioniBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransazioneViewModel by viewModels()
    private lateinit var adapter: AdapterTransazioni

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
        adapter = AdapterTransazioni { transazione -> }
        binding.recyclerTransazioni.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerTransazioni.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    adapter.submitList(state.transazioni)
                    binding.textVuoto.visibility =
                        if (state.transazioni.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}