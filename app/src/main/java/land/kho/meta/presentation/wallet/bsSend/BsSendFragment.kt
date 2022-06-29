package land.kho.meta.presentation.wallet.bsSend

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import land.kho.meta.R
import land.kho.meta.base.BaseBS
import land.kho.meta.databinding.FragmentBsSendBinding
import land.kho.meta.presentation.wallet.bsReceipt.BsReceiptFragment
import land.kho.meta.presentation.wallet.bsReceipt.ReceiptData
import land.kho.meta.utils.AnalyticsConstants
import land.kho.meta.utils.AnalyticsLogger
import land.kho.wallet.KhoWallet

@AndroidEntryPoint
class BsSendFragment : BaseBS<FragmentBsSendBinding, BsSendVM>() {

    private val bsSendVM: BsSendVM by viewModels()
    lateinit var args: String

    companion object {
        fun getNewInstance(
            accountAddress: String
        ): BsSendFragment {
            return BsSendFragment().apply {
                this.args = accountAddress
            }
        }
    }

    override fun layoutRes() = R.layout.fragment_bs_send

    override fun viewModelClass() = bsSendVM

    override fun bindViews() {
        binding.viewModel = viewModel
        setUpClickListener()
    }

    private fun setUpClickListener() {

//        binding.btnSend.setOnClickListener {
//            AnalyticsLogger.logEvent(
//                screen = AnalyticsConstants.ScreenName.BS_SEND_MONEY,
//                event = AnalyticsConstants.Event.ACCEPT,
//                value = AnalyticsConstants.Value.BUTTON_CLICKED
//            )
//            if (KhoWallet.checkAddress(accountAddress = binding.etAccountAddress.text.toString())) {
//                BsReceiptFragment.getNewInstance(
//                    receiptData = ReceiptData(
//                        amount = binding.etAmount.text.toString(),
//                        to = binding.etAccountAddress.text.toString(),
//                        from = viewModel.accountAddress
//                    )
//                ).show(childFragmentManager, "BsReceiptFragment")
//            } else {
//                binding.evAccountAddress.error = "invalid address"
//            }
//
//        }
//        binding.btnCancel.setOnClickListener {
//            AnalyticsLogger.logEvent(
//                screen = AnalyticsConstants.ScreenName.BS_SEND_MONEY,
//                event = AnalyticsConstants.Event.CANCEL,
//                value = AnalyticsConstants.Value.BUTTON_CLICKED
//            )
//            closeDialog()
//        }
    }
}

