package land.kho.meta.presentation.wallet.bsReceive

import android.content.Intent
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import land.kho.meta.R
import land.kho.meta.base.BaseBS
import land.kho.meta.databinding.FragmentBsReceiveBinding
import land.kho.meta.utils.AnalyticsConstants
import land.kho.meta.utils.AnalyticsLogger
import land.kho.meta.utils.Utility

@AndroidEntryPoint
class BsReceiveFragment : BaseBS<FragmentBsReceiveBinding, BsReceiveVM>() {

    private val bsReceiveVM: BsReceiveVM by viewModels()
    lateinit var args: String

    companion object {
        fun getNewInstance(
            accountAddress: String
        ): BsReceiveFragment {
            return BsReceiveFragment().apply {
                this.args = accountAddress
            }
        }
    }

    override fun layoutRes() = R.layout.fragment_bs_receive

    override fun viewModelClass() = bsReceiveVM

    override fun bindViews() {
        binding.viewModel = viewModel
        binding.tvAccountAddress.text = args
        viewModel.getQrCode(args = args)
        setUpClickListener()
    }

    private fun setUpClickListener() {

        binding.ivCopy.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.BS_RECEIVE_MONEY,
                event = AnalyticsConstants.Event.ADDRESS_COPY,
                value = AnalyticsConstants.Value.COPY_CLICKED
            )
            Utility.copyText(text = args)
        }

        binding.btnShare.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.BS_RECEIVE_MONEY,
                event = AnalyticsConstants.Event.ADDRESS_SHARED,
                value = AnalyticsConstants.Value.BUTTON_CLICKED
            )
            shareAddress()
        }
    }

    private fun shareAddress() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Account Address")
        intent.putExtra(Intent.EXTRA_TEXT, args)
        startActivity(intent)
    }

}

