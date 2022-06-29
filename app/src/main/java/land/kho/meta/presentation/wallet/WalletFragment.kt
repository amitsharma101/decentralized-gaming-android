package land.kho.meta.presentation.wallet

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import land.kho.meta.R
import land.kho.meta.base.BaseFragment
import land.kho.meta.databinding.FragmentWalletBinding
import land.kho.meta.presentation.application.AppApplication
import land.kho.meta.presentation.wallet.bsReceipt.BsReceiptFragment
import land.kho.meta.presentation.wallet.bsReceipt.ReceiptData
import land.kho.meta.presentation.wallet.bsReceive.BsReceiveFragment
import land.kho.meta.presentation.wallet.bsSend.BsSendFragment
import land.kho.meta.presentation.wallet.bsVerify.BsVerifyFragment
import land.kho.meta.utils.*
import land.kho.wallet.KhoWallet

@AndroidEntryPoint
@SuppressLint("NotifyDataSetChanged")
class WalletFragment : BaseFragment<FragmentWalletBinding, WalletVM>(),
    ItemClickListener<TransactionAdapterVM> {

    private val walletVM: WalletVM by viewModels()
    private lateinit var adapter: CommonAdapter<TransactionAdapterVM>
    private var adapterList: MutableList<TransactionAdapterVM> = ArrayList()

    override fun layoutRes() = R.layout.fragment_wallet

    override fun viewModelClass() = walletVM

    override fun bindViews() {
        binding.viewModel = viewModel
        adapter = CommonAdapter(adapterList, this)
        binding.rvTransaction.adapter = adapter
        showLoading()
        viewModel.getMnemonic()
        viewModel.getTokenTransactions()
        binding.tvEmpty.text = resources.getString(R.string.empty_text, "NFTs")
        bindObservers()
    }

    override fun onItemClick(value: TransactionAdapterVM) {
        AnalyticsLogger.logEvent(
            screen = AnalyticsConstants.ScreenName.WALLET,
            event = AnalyticsConstants.Event.VIEW_TRANSACTION,
            value = AnalyticsConstants.Value.ITEM_CLICKED
        )
        BsReceiptFragment.getNewInstance(receiptData = ReceiptData(model = value.model))
            .show(childFragmentManager, "BsReceiptFragment")
    }

    private fun setUpClickListener() {

        binding.btnReceive.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.WALLET,
                event = AnalyticsConstants.Event.RECEIVE_MONEY,
                value = AnalyticsConstants.Value.BUTTON_CLICKED
            )
            BsReceiveFragment.getNewInstance(accountAddress = viewModel.address)
                .show(childFragmentManager, "BsReceiveFragment")
        }

        binding.btnSend.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.WALLET,
                event = AnalyticsConstants.Event.SEND_MONEY,
                value = AnalyticsConstants.Value.BUTTON_CLICKED
            )
            BsSendFragment.getNewInstance(accountAddress = viewModel.address)
                .show(childFragmentManager, "BsSendFragment")
        }

        binding.ivCopy.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.WALLET,
                event = AnalyticsConstants.Event.ADDRESS_COPY,
                value = AnalyticsConstants.Value.COPY_CLICKED
            )
            Utility.copyText(text = viewModel.address)
        }

        binding.btnVerify.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.WALLET,
                event = AnalyticsConstants.Event.VERIFY_EMAIL,
                value = AnalyticsConstants.Value.BUTTON_CLICKED
            )
            BsVerifyFragment.getNewInstance(
                email = viewModel.email,
                isHavingEmail = viewModel.email.isNotBlank(),
            ) { email ->
                if (email.isNotBlank()) {
                    viewModel.email = email
                }
            }.show(childFragmentManager, "BsVerifyFragment")
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 1) {
                    AnalyticsLogger.logEvent(
                        screen = AnalyticsConstants.ScreenName.WALLET,
                        event = AnalyticsConstants.Event.TOKEN_TRANSACTIONS_TAB,
                        value = AnalyticsConstants.Value.BUTTON_CLICKED
                    )
                    viewModel.getTokenTransactions()
                }
//                 else { // todo will show nft latter
//                    AnalyticsLogger.logEvent(
//                        screen = AnalyticsConstants.ScreenName.WALLET,
//                        event = AnalyticsConstants.Event.NFT_TRANSACTIONS_TAB,
//                        value = AnalyticsConstants.Value.BUTTON_CLICKED
//                    )
//                    viewModel.getNftTransactions()
//                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }


    private fun bindObservers() {

        viewModel.accountAddress.observe(viewLifecycleOwner) { event ->
            event?.getContentIfNotHandled()?.let { accountAddress ->
                if (accountAddress.isNotBlank()) {
                    binding.tvAddressValue.text = accountAddress
                    if (AppApplication.connectivityManager.isNetworkAvailable.value == true) {
                        val balance = KhoWallet.getBalance(
                            accountAddress = accountAddress,
                            mnemonic = viewModel.mnemonic
                        )
                        binding.tvBalanceValue.text = Utility.toAmount(amount = balance)
                    }
                    dismissLoading()
                    setUpClickListener()
                }
            }
        }
        viewModel.tokenTransactionsList.observe(viewLifecycleOwner) { event ->
            event?.getContentIfNotHandled()?.let { transactions ->
                if (transactions.isEmpty()) {
                    binding.tvEmpty.text = resources.getString(R.string.empty_text, "transactions")
                    binding.tvEmpty.visibleOrGone(isVisible = true)
                    binding.rvTransaction.visibleOrGone(isVisible = false)
                } else {
                    binding.rvTransaction.visibleOrGone(isVisible = true)
                    binding.tvEmpty.visibleOrGone(isVisible = false)
                    adapterList.clear()
                    transactions.forEach { transaction ->
                        if (transaction.from == viewModel.address) {
                            transaction.type = Constants.SEND_TO
                            transaction.sender = Utility.toShortAddress(address = transaction.to)
                        } else {
                            transaction.type = Constants.RECEIVE_FROM
                            transaction.sender = Utility.toShortAddress(address = transaction.from)
                        }
                        transaction.date = Utility.renderToDate(date = transaction.timeStamp)
                        transaction.balance = Utility.toAmount(
                            amount = transaction.value ?: "0",
                            symbol = transaction.tokenSymbol
                        )
                        adapterList.add(
                            TransactionAdapterVM(
                                model = transaction,
                                viewType = R.layout.item_transactions
                            )
                        )
                    }
                    adapter.notifyDataSetChanged()
                }

            }
        }

        viewModel.nftTransactionsList.observe(viewLifecycleOwner) { event ->
            event?.getContentIfNotHandled()?.let { transactions ->
                if (transactions.isEmpty()) {
                    binding.tvEmpty.text = resources.getString(R.string.empty_text, "NFTs")
                    binding.tvEmpty.visibleOrGone(isVisible = true)
                    binding.rvTransaction.visibleOrGone(isVisible = false)
                } else {
                    binding.rvTransaction.visibleOrGone(isVisible = true)
                    binding.tvEmpty.visibleOrGone(isVisible = false)
                    adapterList.clear()
                    transactions.forEach { transaction ->
                        if (transaction.from == viewModel.address) {
                            transaction.type = Constants.SEND_TO
                            transaction.sender = Utility.toShortAddress(address = transaction.to)
                        } else {
                            transaction.type = Constants.RECEIVE_FROM
                            transaction.sender = Utility.toShortAddress(address = transaction.from)
                        }
                        transaction.date = Utility.renderToDate(date = transaction.timeStamp)
                        transaction.balance = "${transaction.tokenSymbol} : ${transaction.tokenID}"
                        adapterList.add(
                            TransactionAdapterVM(
                                model = transaction,
                                viewType = R.layout.item_transactions
                            )
                        )
                    }
                    adapter.notifyDataSetChanged()
                }

            }
        }
    }
}