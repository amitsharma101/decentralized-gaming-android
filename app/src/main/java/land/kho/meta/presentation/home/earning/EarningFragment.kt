package land.kho.meta.presentation.home.earning

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import land.kho.meta.R
import land.kho.meta.base.BaseFragment
import land.kho.meta.databinding.FragmentEarningBinding
import land.kho.meta.presentation.bsInfo.BsInfoFragment
import land.kho.meta.presentation.bsInfo.InfoData
import land.kho.meta.utils.*

@AndroidEntryPoint
@SuppressLint("NotifyDataSetChanged")
class EarningFragment : BaseFragment<FragmentEarningBinding, EarningVM>(),
    ItemClickListener<EarningsAdapterVM> {

    private val earningVM: EarningVM by viewModels()
    private lateinit var adapter: CommonAdapter<EarningsAdapterVM>
    private var adapterList: MutableList<EarningsAdapterVM> = ArrayList()

    override fun layoutRes() = R.layout.fragment_earning

    override fun viewModelClass() = earningVM

    override fun bindViews() {
        binding.viewModel = viewModel
        adapter = CommonAdapter(adapterList, this)
        binding.rvTransaction.adapter = adapter
        showLoading()
        viewModel.getWalletBalance()
        viewModel.getEarnings()
        setUpClickListener()
        bindObservers()
    }

    override fun onItemClick(value: EarningsAdapterVM) {

    }

    private fun bindObservers() {
        viewModel.earningsList.observe(viewLifecycleOwner) { event ->
            event?.getContentIfNotHandled()?.let { earnings ->
                if (earnings.isEmpty()) {
                    binding.tvEmpty.text = resources.getString(R.string.empty_text, "earnings")
                    binding.tvEmpty.visibleOrGone(isVisible = true)
                    binding.rvTransaction.visibleOrGone(isVisible = false)
                } else {
                    binding.rvTransaction.visibleOrGone(isVisible = true)
                    binding.tvEmpty.visibleOrGone(isVisible = false)
                    adapterList.clear()
                    earnings.forEach { earning ->
                        earning.text = Utility.toEarningText(earning = earning)
                        adapterList.add(
                            EarningsAdapterVM(
                                model = earning,
                                viewType = R.layout.item_earnings
                            )
                        )
                    }
                    adapter.notifyDataSetChanged()
                }
                dismissLoading()
            }
        }
    }

    private fun setUpClickListener() {
        binding.btnTransfer.setOnClickListener {
            if (viewModel.isVerified) {
                AnalyticsLogger.logEvent(
                    screen = AnalyticsConstants.ScreenName.EARNING,
                    event = AnalyticsConstants.Event.TRANSFER_KHO,
                    value = AnalyticsConstants.Value.BUTTON_CLICKED
                )
                showLoading()
               viewModel.transferToWallet()
            } else {
                AnalyticsLogger.logEvent(
                    screen = AnalyticsConstants.ScreenName.EARNING,
                    event = AnalyticsConstants.Event.VERIFY_EMAIL,
                    value = AnalyticsConstants.Value.BUTTON_CLICKED
                )
                BsInfoFragment.getNewInstance(
                    infoData = InfoData(
                        text = "Go to wallet and get verified first before transferring earning to wallet"
                    )
                ).show(childFragmentManager, "BsInfoFragment")
            }
        }
    }

}