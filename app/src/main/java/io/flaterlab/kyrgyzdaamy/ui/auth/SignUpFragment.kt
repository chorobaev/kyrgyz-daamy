package io.flaterlab.kyrgyzdaamy.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.kyrgyzdaamy.R
import io.flaterlab.kyrgyzdaamy.databinding.SignUpFragmentBinding
import io.flaterlab.kyrgyzdaamy.extension.loadingHide
import io.flaterlab.kyrgyzdaamy.extension.loadingShow
import io.flaterlab.kyrgyzdaamy.extension.toast
import io.flaterlab.kyrgyzdaamy.service.AppPreferences
import io.flaterlab.kyrgyzdaamy.ui.MainActivity
import io.flaterlab.kyrgyzdaamy.ui.viewBinding
import org.koin.android.ext.android.bind
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.sign_up_fragment) {


    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    val binding by viewBinding(SignUpFragmentBinding::bind)

    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private var storedVerificationId: String? = ""

    private var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    private var isCodeSent = false

    private var phoneNumber: String = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as MainActivity

        activity.hideBottomNav()


        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d("Verification", "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w("Verification", "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    toast("Invalid request")
                } else if (e is FirebaseTooManyRequestsException) {
                    toast("The SMS quota for the project has been exceeded")
                }

                toast("Verification failed ${e.message}")
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                isCodeSent = true
                AppPreferences.phoneNumber = binding.phoneNumberEditText.text.toString()
                binding.phoneNumber.text = binding.phoneNumberEditText.text.toString()
                binding.groupPhoneNumber.visibility =View.GONE
                binding.groupOtp.visibility = View.VISIBLE
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("Verification", "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token
            }
        }

        binding.verifyButton.setOnClickListener {
            if (isCodeSent) {
                if (binding.pinView.text.toString().length == 6) {
                    val credentials = PhoneAuthProvider.getCredential(
                        storedVerificationId!!,
                        binding.pinView.text.toString()
                    )
                    signInWithPhoneAuthCredential(credentials)
                }else{
                    binding.pinView.error = "Введите код полностью"
                }
            }
        }
        binding.pinView.addTextChangedListener {
            binding.pinView.error = null
        }
        binding.phoneNumberEditText.addTextChangedListener {
            binding.phoneNumberLayout.isErrorEnabled = false
        }

        binding.nextButton.setOnClickListener {
            val digits = binding.phoneNumberEditText.text?.filter {
                it.isDigit()
            }

            if (digits?.length == 12) {
                phoneNumber = binding.phoneNumberEditText.text.toString()
                val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                    .setPhoneNumber(binding.phoneNumberEditText.text.toString())       // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(requireActivity())                 // Activity (for callback binding)
                    .setCallbacks(callbacks!!)          // OnVerificationStateChangedCallbacks
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            }else{
                binding.phoneNumberLayout.error = "Введите номер полностью"
            }

        }

        binding.resentOtp.setOnClickListener {
            resendVerificationCode(phoneNumber, resendToken)
        }
    }

    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(callbacks!!)          // OnVerificationStateChangedCallbacks
        if (token != null) {
            optionsBuilder.setForceResendingToken(token) // callback's ForceResendingToken
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        loadingShow()
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    loadingHide()
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SignIn", "signInWithCredential:success")
                    findNavController().navigate(SignUpFragmentDirections.toMobileNavigation())
                } else {
                    loadingHide()
                    // Sign in failed, display a message and update the UI
                    Log.w("SignIn", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        toast("The verification code entered was invalid")
                    } else {
                        toast("Verification failed: ${task.exception!!.message}")
                    }

                }
            }
    }
}