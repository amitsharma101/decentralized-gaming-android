package land.kho.meta.presentation.wallet.createWallet

import android.content.Intent
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import land.kho.meta.R
import land.kho.meta.base.BaseFragment
import land.kho.meta.databinding.FragmentCreateWalletBinding
import land.kho.meta.presentation.bsLoading.BsLoading
import land.kho.meta.utils.AnalyticsConstants
import land.kho.meta.utils.AnalyticsLogger
import land.kho.meta.utils.Utility
import land.kho.meta.utils.Utility.hideKeyboard
import land.kho.meta.utils.visibleOrGone
import land.kho.wallet.KhoWallet

@AndroidEntryPoint
class CreateWalletFragment : BaseFragment<FragmentCreateWalletBinding, CreateWalletVM>() {

    private val createWalletVM: CreateWalletVM by viewModels()
    private val args: CreateWalletFragmentArgs by navArgs()

    override fun layoutRes() = R.layout.fragment_create_wallet

    override fun viewModelClass() = createWalletVM

    override fun bindViews() {
        binding.viewModel = viewModel
        viewModel.createWallet.set(args.createWallet)
        initFcmToken()
        setUpClickListener()
        bindObservers()
    }

    private fun initFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new FCM registration token
            viewModel.fcmToken = task.result
        })
    }

    private fun setUpClickListener() {
        binding.btnCreate.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.WALLET,
                event = AnalyticsConstants.Event.CREATE_WALLET,
                value = AnalyticsConstants.Value.BUTTON_CLICKED
            )
            viewModel.saveAccount(
                khoWalletAccount = KhoWallet.createWallet()
            )
        }

        binding.btnImportExisting.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.WALLET,
                event = AnalyticsConstants.Event.IMPORT_WALLET,
                value = AnalyticsConstants.Value.BUTTON_CLICKED
            )
            binding.clWallet.visibleOrGone(isVisible = false)
            binding.clImportWallet.visibleOrGone(isVisible = true)
        }

        binding.ivCopy.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.WALLET,
                event = AnalyticsConstants.Event.MNEMONIC_COPY,
                value = AnalyticsConstants.Value.COPY_CLICKED
            )
            Utility.copyText(text = viewModel.mnemonic.get()!!)
        }

        binding.btnImport.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.WALLET,
                event = AnalyticsConstants.Event.MNEMONIC_ADDED,
                value = AnalyticsConstants.Value.BUTTON_CLICKED
            )
            if (KhoWallet.validateMnemonic(mnemonic = binding.etMnemonic.text.toString())) {
                viewModel.saveAccount(
                    khoWalletAccount = KhoWallet.restoreWallet(mnemonic = binding.etMnemonic.text.toString())
                )
                binding.etMnemonic.hideKeyboard()
                binding.clImportWallet.visibleOrGone(isVisible = false)
            } else {
                binding.evMnemonic.error = "Mnemonic should be 12 words separated by space."
            }
        }

        binding.btnSave.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.WALLET,
                event = AnalyticsConstants.Event.MNEMONIC_SHARED,
                value = AnalyticsConstants.Value.BUTTON_CLICKED
            )
            shareAddress()
        }

        binding.btnSaved.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.WALLET,
                event = AnalyticsConstants.Event.MNEMONIC_SAVED,
                value = AnalyticsConstants.Value.BUTTON_CLICKED
            )
            viewModel.loadingSheet = BsLoading.getNewInstance(
                message = "Signing with crypto wallet.",
                loading = true,
                retry = false
            ) {
                if (it) {
                    viewModel.signIn()
                }
            }
            viewModel.loadingSheet.show(childFragmentManager, "BsLoading")
            viewModel.signIn()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    handleBackPress()
                }
            }
        )
    }

    private fun handleBackPress() {
        if (binding.clImportWallet.visibility == 0) {
            binding.clWallet.visibleOrGone(isVisible = true)
            binding.clImportWallet.visibleOrGone(isVisible = false)
        } else {
            requireActivity().finish()
        }
    }

    private fun shareAddress() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Secret Recovery Phrase")
        intent.putExtra(Intent.EXTRA_TEXT, viewModel.mnemonic.get()!!)
        startActivity(intent)
    }

    private fun bindObservers() {
        viewModel.mnemonicSaved.observe(viewLifecycleOwner) { event ->
            event?.getContentIfNotHandled()?.let { isMnemonicSaved ->
                if (isMnemonicSaved) {
                    findNavController().navigate(R.id.action_create_wallet_fragment_to_my_game_fragment)
                }
            }
        }
    }

}