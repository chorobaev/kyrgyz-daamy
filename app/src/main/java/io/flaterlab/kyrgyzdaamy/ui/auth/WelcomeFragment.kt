package io.flaterlab.kyrgyzdaamy.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import io.flaterlab.kyrgyzdaamy.R
import io.flaterlab.kyrgyzdaamy.databinding.WelcomeFragmentBinding
import io.flaterlab.kyrgyzdaamy.ui.MainActivity
import io.flaterlab.kyrgyzdaamy.ui.viewBinding

class WelcomeFragment : Fragment(R.layout.welcome_fragment) {

    val binding by viewBinding(WelcomeFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.signIn.setOnClickListener {
            findNavController().navigate(R.id.nav_sign_up)
        }

    }

}