package land.kho.meta.presentation.wallet.bsSummary

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import land.kho.meta.R
import land.kho.meta.base.BaseBS
import land.kho.meta.databinding.FragmentBsSummaryBinding
import land.kho.meta.utils.AnalyticsConstants
import land.kho.meta.utils.AnalyticsLogger
import land.kho.meta.utils.Utility

@AndroidEntryPoint
class BsSummaryFragment : BaseBS<FragmentBsSummaryBinding, BsSummaryVM>() {

    private val bsSummaryVM: BsSummaryVM by viewModels()
    private lateinit var accountAddress: String
    private lateinit var balance: String
    private lateinit var unverifiedTokens: String
    private var isVerified = false

    companion object {
        fun getNewInstance(
            accountAddress: String,
            balance: String,
            unverifiedTokens: String,
            isVerified: Boolean
        ): BsSummaryFragment {
            return BsSummaryFragment().apply {
                this.accountAddress = accountAddress
                this.balance = balance
                this.unverifiedTokens = unverifiedTokens
                this.isVerified = isVerified
            }
        }
    }

    override fun layoutRes() = R.layout.fragment_bs_summary

    override fun viewModelClass() = bsSummaryVM

    override fun bindViews() {
        binding.viewModel = viewModel
        setUpClickListener()
        binding.tvAddressValue.text = accountAddress
        binding.tvBalanceValue.text = balance
        viewModel.isVerified.set(isVerified)
        viewModel.unverifiedTokens = unverifiedTokens
    }

    private fun setUpClickListener() {
        binding.btnWallet.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.BS_WALLET_SUMMARY,
                event = AnalyticsConstants.Event.GO_TO_WALLET,
                value = AnalyticsConstants.Value.BUTTON_CLICKED
            )
            findNavController().navigate(R.id.action_meta_fragment_to_wallet_fragment)
            dismiss()
        }
        binding.ivCopy.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.BS_WALLET_SUMMARY,
                event = AnalyticsConstants.Event.ADDRESS_COPY,
                value = AnalyticsConstants.Value.COPY_CLICKED
            )
            Utility.copyText(text = accountAddress)
        }
    }

}