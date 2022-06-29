package land.kho.meta.presentation.login

import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import dagger.hilt.android.AndroidEntryPoint
import land.kho.meta.R
import land.kho.meta.base.BaseFragment
import land.kho.meta.databinding.FragmentLogInBinding
import land.kho.meta.utils.Utility
import land.kho.meta.utils.visibleOrGone
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLogInBinding, LoginVM>() {

    private val loginVM: LoginVM by viewModels()

    private lateinit var auth: FirebaseAuth
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks


    override fun layoutRes() = R.layout.fragment_log_in

    override fun viewModelClass() = loginVM

    override fun bindViews() {
        binding.viewModel = viewModel
        initFirebase()
        setCountryCodeSpinner()
        setUpClickListener()
    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                //Utility.log(TAG, "onVerificationCompleted:$credential")
                // signInWithCredential(credential)
                val otp = credential.smsCode
                if (otp != null) {
                    binding.etEnterOtp.setText(otp)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                //Utility.log(TAG, "onVerificationFailed", e)

                when (e) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        Utility.showToast(e.message ?: "") // Invalid request
                    }
                    is FirebaseTooManyRequestsException -> {
                        Utility.showToast(
                            e.message ?: ""
                        ) // The SMS quota for the project has been exceeded
                    }
                }
                dismissLoading()

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                //Utility.log(TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                binding.clEnterNumber.visibleOrGone(isVisible = false)
                binding.btnOtp.visibleOrGone(isVisible = false)
                binding.evEnterOtp.visibleOrGone(isVisible = true)
                binding.btnLogin.visibleOrGone(isVisible = true)
                binding.tvEnter.text = "Please enter OTP"
                dismissLoading()
                storedVerificationId = verificationId
                resendToken = token
            }
        }

    }

    private fun setUpClickListener() {

        binding.btnOtp.setOnClickListener {
            val number = binding.etEnterNumber.text.toString()
            if (number.isNotBlank()) {
                // call otp
                // cc "${bindingView.acsCountryCode?.selectedItem.toString()}"

                viewModel.number = binding.spinnerCountryCode.selectedItem.toString()
                    .substringAfterLast(" ") + number
                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(viewModel.number)       // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(requireActivity())                 // Activity (for callback binding)
                    .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
                showLoading()
            } else {
                binding.etEnterNumber.error = "Invalid mobile number"
            }
        }

        binding.btnLogin.setOnClickListener {
            val otp = binding.etEnterOtp.text.toString()
            if (otp.isNotBlank()) {
                signInWithCredential(PhoneAuthProvider.getCredential(storedVerificationId!!, otp))
            } else {
                binding.etEnterNumber.error = "Invalid mobile number"
            }
        }
    }

    private fun setCountryCodeSpinner() {
        val list = Utility.getCountryCodeList()
        list.subList(1, list.size).sort()
        binding.spinnerCountryCode.adapter =
            ArrayAdapter(requireContext(), R.layout.item_country_code, list)

    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        showLoading()
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Utility.showToast("logged in")
                    viewModel.saveInDB {
                        if (it) {
                            findNavController().navigate(
                                LoginFragmentDirections.actionLoginFragmentToCreateWalletFragment(
                                    true
                                )
                            )
                        }
                    }
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Utility.showToast(task.exception?.message ?: "")
                    }
                }
                dismissLoading()
            }
    }


}