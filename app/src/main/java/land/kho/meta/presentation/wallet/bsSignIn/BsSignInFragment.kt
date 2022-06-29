package land.kho.meta.presentation.wallet.bsSignIn

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import land.kho.meta.R
import land.kho.meta.base.BaseBS
import land.kho.meta.databinding.FragmentBsSignInBinding
import land.kho.meta.utils.AnalyticsConstants
import land.kho.meta.utils.AnalyticsLogger
import land.kho.wallet.KhoWallet


@AndroidEntryPoint
class BsSignInFragment : BaseBS<FragmentBsSignInBinding, BsSignInVM>() {

    private val bsSignInVM: BsSignInVM by viewModels()
    lateinit var mnemonic: String
    lateinit var listener: SignBottomSheetInterface

    companion object {
        fun getNewInstance(
            text: String,
            listener: SignBottomSheetInterface
        ): BsSignInFragment {
            return BsSignInFragment().apply {
                this.mnemonic = text
                this.listener = listener
            }
        }
    }

    override fun layoutRes() = R.layout.fragment_bs_sign_in

    override fun viewModelClass() = bsSignInVM

    override fun bindViews() {
        binding.viewModel = viewModel
        initView()
        setUpClickListener()
        setUpObservers()
    }

    private fun initView(){
        viewModel.getNonce()
    }

    private fun setUpObservers() {
        viewModel.nonce.observe(viewLifecycleOwner) {
            binding.tvText.text = viewModel.message
        }

        viewModel.token.observe(viewLifecycleOwner) {
            if (it.isNotBlank()) {
                listener.onTokenSaved()
                closeDialog()
            }
        }
    }

    private fun setUpClickListener() {
        binding.btnSign.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.BS_SIGN,
                event = AnalyticsConstants.Event.ACCEPT,
                value = AnalyticsConstants.Value.BUTTON_CLICKED
            )
            val signature = KhoWallet.signMessage(viewModel.message, mnemonic)
            viewModel.getToken(signature)
        }

        binding.btnCancel.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.BS_SIGN,
                event = AnalyticsConstants.Event.CANCEL,
                value = AnalyticsConstants.Value.BUTTON_CLICKED
            )
            closeDialog()
        }
    }
}

interface SignBottomSheetInterface {
    fun onTokenSaved()
}