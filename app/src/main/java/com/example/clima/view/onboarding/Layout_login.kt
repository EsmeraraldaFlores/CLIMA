package com.example.clima.view.onboarding

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.clima.R
import com.example.clima.core.LocationProvider
import com.example.clima.databinding.FragmentLayoutLoginBinding
import com.example.clima.view.home.FragmentComunicator
import com.example.clima.view.home.HomeActivity
import com.example.clima.viewModel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class layout_login : Fragment() {

    @Inject lateinit var locationProvider: LocationProvider

    private var _binding: FragmentLayoutLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SignInViewModel>()
    private lateinit var communicator: FragmentComunicator
    var isValid: Boolean = false

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fine = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarse = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (fine || coarse) {
            getUserLocation()
        } else {
            Toast.makeText(requireContext(), "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        communicator = requireActivity() as HomeActivity
        _binding = FragmentLayoutLoginBinding.inflate(inflater, container, false)
        setupView()
        setupObservers()
        return binding.root
    }

    private fun setupView() {
        getUserLocation()

        binding.textView2.setOnClickListener {
            findNavController().navigate(R.id.action_layout_login_to_layout_register)
        }
        binding.textView.setOnClickListener {
            findNavController().navigate(R.id.action_layout_login_to_restorePassword)
        }
        binding.filledButton.setOnClickListener {
            if (validateInputs()) {
                requestLogin()
            } else {
                Toast.makeText(activity, "Correo y contraseña son obligatorios", Toast.LENGTH_SHORT).show()
            }
        }

        binding.etCorreo.addTextChangedListener {
            binding.tilCorreo.error = if (it.isNullOrEmpty()) "Por favor introduce un correo" else null
            isValid = validateInputs()
        }

        binding.etContrasenia.addTextChangedListener {
            binding.tilContrasenia.error = if (it.isNullOrEmpty()) "Por favor introduce una contraseña" else null
            isValid = validateInputs()
        }
    }

    private fun setupObservers() {
        viewModel.loaderState.observe(viewLifecycleOwner) {
            communicator.showLoader(it)
        }

        viewModel.sessionValid.observe(viewLifecycleOwner) { valid ->
            if (valid) {
                val bundle = Bundle().apply {
                    putString("email", binding.etCorreo.text?.toString() ?: "")
                }
                findNavController().navigate(R.id.action_layout_login_to_weatherFragment, bundle)
            } else {
                Toast.makeText(activity, "Ingreso inválido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInputs(): Boolean {
        val emailValid = binding.etCorreo.text.toString().isNotEmpty()
        val passValid = binding.etContrasenia.text.toString().isNotEmpty()
        return emailValid && passValid
    }

    private fun requestLogin() {
        val email = binding.etCorreo.text.toString().trim()
        val pass = binding.etContrasenia.text.toString().trim()

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(activity, "Correo y contraseña son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.requestSignIn(email, pass)
    }

    private fun getUserLocation() {
        if (!hasLocationPermission()) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            return
        }

        lifecycleScope.launch {
            try {
                val location = locationProvider.getCurrentLocation()
                if (location != null) {
                    Log.i("LOCATION", "Ubicación obtenida: ${location.latitude}, ${location.longitude}")
                } else {
                    Log.e("LOCATION", "Ubicación nula")
                    Toast.makeText(requireContext(), "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("LOCATION", "Error: ${e.message}")
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        val ctx = requireContext()
        return ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
