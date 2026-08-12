package com.example.spesapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
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
        } else {
            val manager = BiometricManager.from(requireContext())
            if (manager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK) ==
                BiometricManager.BIOMETRIC_SUCCESS
            ) {
                binding.btnImpronta.visibility = View.VISIBLE
                binding.btnImpronta.setOnClickListener { mostraImpronta() }
            }
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

    private fun mostraImpronta() {
        val prompt = BiometricPrompt(
            this,
            ContextCompat.getMainExecutor(requireContext()),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    vaiAllaLista()
                }
            }
        )
        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Sblocca SpesApp")
            .setSubtitle("Usa la tua impronta digitale")
            .setNegativeButtonText("Usa il PIN")
            .build()
        prompt.authenticate(info)
    }

    private fun vaiAllaLista() {
        findNavController().navigate(R.id.action_pin_to_lista)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}