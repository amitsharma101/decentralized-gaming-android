package land.kho.meta.presentation.wallet.bsReceipt

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import land.kho.meta.R
import land.kho.meta.base.BaseBS
import land.kho.meta.databinding.FragmentBsReceiptBinding
import land.kho.meta.presentation.application.AppApplication
import land.kho.meta.utils.*
import land.kho.wallet.KhoWallet

private const val TAG = "BsReceiptFragment"

@AndroidEntryPoint
class BsReceiptFragment : BaseBS<FragmentBsReceiptBinding, BsReceiptVM>(),
    ItemClickListener<ReceiptAdapterVM> {

    private val bsReceiptVM: BsReceiptVM by viewModels()
    private lateinit var adapter: CommonAdapter<ReceiptAdapterVM>
    private var adapterList: MutableList<ReceiptAdapterVM> = ArrayList()
    private lateinit var receiptData: ReceiptData

    companion object {
        fun getNewInstance(
            receiptData: ReceiptData
        ): BsReceiptFragment {
            return BsReceiptFragment().apply {
                this.receiptData = receiptData
            }
        }
    }

    override fun layoutRes() = R.layout.fragment_bs_receipt

    override fun viewModelClass() = bsReceiptVM

    override fun bindViews() {
        binding.viewModel = viewModel
        adapter = CommonAdapter(adapterList, this)
        binding.rvReceipt.adapter = adapter
        viewModel.receiptData.set(receiptData)
        setUpClickListener()
        bindObservers()
    }

    private fun setUpClickListener() {

        binding.btnAccept.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.BS_RECEIPT,
                event = AnalyticsConstants.Event.ACCEPT,
                value = AnalyticsConstants.Value.BUTTON_CLICKED
            )
            viewModel.getMnemonic { mnemonic ->
                if (AppApplication.connectivityManager.isNetworkAvailable.value == true) {
                    val transfer = KhoWallet.transfer(
                        amount = viewModel.modal.value!!.to,
                        toAddress = viewModel.modal.value!!.to,
                        mnemonic = mnemonic
                    )
                    Utility.showToast(msg = "transfer : $transfer")
                    Utility.log(tag = TAG, msg = "transfer : $transfer")
                    closeDialog()
                }
            }

        }
        binding.tvView.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.BS_RECEIPT,
                event = AnalyticsConstants.Event.TRANSACTION_OPEN_IN_BROWSER,
                value = AnalyticsConstants.Value.BUTTON_CLICKED
            )
            viewOnBrowser(url = "https://polygonscan.com/tx/${viewModel.modal.value!!.id!!}")
        }
        binding.tvCopy.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.BS_RECEIPT,
                event = AnalyticsConstants.Event.TRANSACTION_ID_COPY,
                value = AnalyticsConstants.Value.COPY_CLICKED
            )
            Utility.copyText(text = viewModel.modal.value!!.id!!)
            closeDialog()
        }
        binding.btnCancel.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.BS_RECEIPT,
                event = AnalyticsConstants.Event.CANCEL,
                value = AnalyticsConstants.Value.BUTTON_CLICKED
            )
            closeDialog()
        }
    }

    override fun onItemClick(value: ReceiptAdapterVM) {

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun bindObservers() {
        viewModel.receiptListUpdated.observe(viewLifecycleOwner) { event ->
            event?.getContentIfNotHandled()?.let { list ->
                adapterList.clear()
                list.forEach { model ->
                    adapterList.add(
                        ReceiptAdapterVM(
                            model
                        )
                    )
                }
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun viewOnBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}