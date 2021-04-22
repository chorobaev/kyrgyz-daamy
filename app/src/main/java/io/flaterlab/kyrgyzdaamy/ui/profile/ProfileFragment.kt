package io.flaterlab.kyrgyzdaamy.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.kyrgyzdaamy.R
import io.flaterlab.kyrgyzdaamy.databinding.ProfileFragmentBinding
import io.flaterlab.kyrgyzdaamy.ui.viewBinding
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment: Fragment(R.layout.profile_fragment) {

    val binding by viewBinding(ProfileFragmentBinding::bind)

    @Inject lateinit var firebaseAuth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signOut.setOnClickListener {
            firebaseAuth.signOut()
            activity?.onBackPressed()
        }
    }

}