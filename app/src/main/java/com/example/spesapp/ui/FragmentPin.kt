package com.example.spesapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.spesapp.R
import com.example.spesapp.databinding.FragmentPinBinding

class FragmentPin : Fragment() {

    private var _binding: FragmentPinBinding? = null
    private val binding get() = _binding!!
    private var inCreazione = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preferenze =
            requireContext().getSharedPreferences("spesapp_prefs", Context.MODE_PRIVATE)
        val pinSalvato = preferenze.getString("pin", null)

        if (pinSalvato == null) {
            inCreazione = true
            binding.textTitoloPin.text = "Crea un PIN"
            binding.editConfermaPin.visibility = View.VISIBLE
            binding.btnConferma.text = "Crea PIN"
        }

        binding.btnConferma.setOnClickListener {
            if (inCreazione) {
                val pin = binding.editPin.text.toString()
                val conferma = binding.editConfermaPin.text.toString()
                if (pin.length != 4) {
                    binding.editPin.error = "Il PIN deve essere di 4 cifre"
                    return@setOnClickListener
                }
                if (pin != conferma) {
                    binding.editConfermaPin.error = "I PIN non coincidono"
                    return@setOnClickListener
                }
                preferenze.edit().putString("pin", pin).apply()
                vaiAllaLista()
            } else {
                val pin = binding.editPin.text.toString()
                if (pin == pinSalvato) {
                    vaiAllaLista()
                } else {
                    binding.editPin.error = "PIN errato"
                    binding.editPin.text?.clear()
                }
            }
        }
    }

    private fun vaiAllaLista() {
        findNavController().navigate(R.id.action_pin_to_lista)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}