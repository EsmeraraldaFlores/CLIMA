package com.example.clima.view.onboarding

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.clima.databinding.ActivityOnboardingBinding
import com.example.clima.view.home.FragmentComunicator
import com.example.clima.R

class OnboardingActivity : AppCompatActivity(), FragmentComunicator {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun showLoader(value: Boolean) {
        binding.loaderContainerView.visibility = if (value) View.VISIBLE else View.GONE
    }

    override fun enviarMensaje(mensaje: String) {
        // Aquí puedes implementar lógica si se requiere usar esta función
    }
}
