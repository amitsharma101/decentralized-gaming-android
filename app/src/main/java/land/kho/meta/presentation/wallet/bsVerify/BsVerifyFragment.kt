package land.kho.meta.presentation.wallet.bsVerify

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import land.kho.meta.R
import land.kho.meta.base.BaseBS
import land.kho.meta.databinding.FragmentBsVerifyBinding
import land.kho.meta.utils.AnalyticsConstants
import land.kho.meta.utils.AnalyticsLogger
import land.kho.meta.utils.Utility.hideKeyboard

@AndroidEntryPoint
class BsVerifyFragment : BaseBS<FragmentBsVerifyBinding, BsVerifyVM>() {

    private val bsVerifyVM: BsVerifyVM by viewModels()
    private var email: String? = null
    private var isHavingEmail = false
    lateinit var emailSendCallback: (email: String) -> Unit

    companion object {
        fun getNewInstance(
            email: String? = null,
            isHavingEmail: Boolean = false,
            emailSendCallback: (email: String) -> Unit
        ): BsVerifyFragment {
            return BsVerifyFragment().apply {
                this.email = email
                this.isHavingEmail = isHavingEmail
                this.emailSendCallback=emailSendCallback
            }
        }
    }

    override fun layoutRes() = R.layout.fragment_bs_verify

    override fun viewModelClass() = bsVerifyVM


    override fun bindViews() {
        binding.viewModel = viewModel
        viewModel.isHavingEmail.set(isHavingEmail)
        viewModel.email = email
        setUpClickListener()
    }

    private fun setUpClickListener() {

        binding.btnSend.setOnClickListener {
            binding.btnSend.hideKeyboard()
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.BS_SEND_EMAIL,
                event = AnalyticsConstants.Event.VERIFY_EMAIL,
                value = AnalyticsConstants.Value.BUTTON_CLICKED
            )
            if (!binding.etEmail.text.isNullOrBlank()) {
                viewModel.sendMail(email = binding.etEmail.text.toString()) {
                    emailSendCallback.invoke(binding.etEmail.text.toString())
                    dismiss()
                }
            } else {
                binding.etEmail.error = "invalid email"
            }

        }
    }
}

