package land.kho.meta.presentation.main

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import land.kho.meta.R
import land.kho.meta.databinding.ActivityMainBinding
import land.kho.meta.presentation.application.AppApplication
import land.kho.meta.presentation.bsInfo.BsInfoFragment
import land.kho.meta.presentation.bsInfo.InfoData
import land.kho.meta.presentation.home.bsUserName.BsUserNameFragment
import land.kho.meta.utils.AnalyticsConstants
import land.kho.meta.utils.AnalyticsLogger
import land.kho.meta.utils.Utility
import land.kho.meta.utils.visibleOrGone
import land.kho.wallet.KhoWallet
import java.math.BigInteger


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private val viewModel: MainActivityVM by viewModels()
    private lateinit var navController: NavController

    private var alertDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.lifecycleOwner = this
        mBinding.viewModel = viewModel
        setUpClickListener()
        setupNavigation()
        bindObservers()
    }

    override fun onStart() {
        super.onStart()
        AppApplication.connectivityManager.registerConnectionObserver(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        AppApplication.connectivityManager.unregisterConnectionObserver(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        when (navController.currentBackStackEntry?.destination?.id) {
            R.id.refer_fragment,
            R.id.earning_fragment,
            R.id.wallet_fragment -> {
                //todo navigate
            }
            else -> {
                super.onBackPressed()
                return
            }
        }
    }

    private fun setupNavigation() {
        navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        supportActionBar?.hide()

        mBinding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.wallet_fragment -> {
                    mBinding.mainLayout.setBackgroundResource(R.color.white)
                    mBinding.cvBottomNavigation.visibleOrGone(isVisible = true)
                    mBinding.cvInfo.visibleOrGone(isVisible = false)
                }
                R.id.my_game_fragment -> {
                    mBinding.mainLayout.setBackgroundResource(R.color.blue)
                    mBinding.cvBottomNavigation.visibleOrGone(isVisible = true)
                    mBinding.cvInfo.visibleOrGone(isVisible = true)
                }
                R.id.refer_fragment,
                R.id.earning_fragment -> {
                    mBinding.mainLayout.setBackgroundResource(R.color.white)
                    mBinding.cvBottomNavigation.visibleOrGone(isVisible = true)
                    mBinding.cvInfo.visibleOrGone(isVisible = true)
                }
                else -> {
                    mBinding.mainLayout.setBackgroundResource(R.color.white)
                    mBinding.cvBottomNavigation.visibleOrGone(isVisible = false)
                    mBinding.cvInfo.visibleOrGone(isVisible = false)
                }
            }
        }
    }

    private fun setUpClickListener() {
        mBinding.clUserName.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.HOME,
                event = AnalyticsConstants.Event.USER_NAME,
                value = AnalyticsConstants.Value.BUTTON_CLICKED
            )
            BsUserNameFragment.getNewInstance(
                name = viewModel.userName.value,
                isHavingName = !viewModel.userName.value.isNullOrBlank(),
            ) { userName ->
                if (userName.isNotBlank()) {
                    viewModel.userName.value = userName
                }
            }.show(supportFragmentManager, "BsVerifyFragment")
        }

        mBinding.clPasses.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.HOME,
                event = AnalyticsConstants.Event.PASS,
                value = AnalyticsConstants.Value.BUTTON_CLICKED
            )
            BsInfoFragment.getNewInstance(
                infoData = InfoData(
                    text = "Shows the no. of games you can play with the current pass"
                )
            ).show(supportFragmentManager, "BsInfoFragment")
        }
    }

    fun showLoading() {
        if (alertDialog != null) {
            alertDialog?.show()
        } else {
            alertDialog = Dialog(this)
            alertDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            alertDialog?.setContentView(R.layout.app_progressbar)
            alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog?.show()
            alertDialog?.setCancelable(false)
        }
    }

    fun dismissLoading() {
        alertDialog?.dismiss()
    }

    fun getNavBarData(
        navBarData: (NavBarData) -> Unit
    ) {
        showLoading()
        viewModel.getAccountAddress {
            val balance = fetchBalance(accountAddress = viewModel.address)
            navBarData.invoke(
                NavBarData(
                    pass = it,
                    balance = balance
                )
            )
        }
    }

    private fun bindObservers() {
        AppApplication.connectivityManager.isNetworkAvailable.observe(this) { isInternetAvailable ->
            if (isInternetAvailable) {
                mBinding.tvError.visibleOrGone(isVisible = false)
            } else {
                mBinding.tvError.visibleOrGone(isVisible = true)
                AppApplication.connectivityManager.pingDNS()
            }
        }

        viewModel.accountAddress.observe(this) { event ->
            event?.getContentIfNotHandled()?.let { accountAddress ->
                if (accountAddress.isNotBlank()) {
                    viewModel.address = accountAddress
                    mBinding.tvBalanceValue.text =
                        Utility.toAmount(amount = fetchBalance(accountAddress = accountAddress))
                    dismissLoading()
                }
            }
        }
    }

    private fun fetchBalance(accountAddress: String): BigInteger {
        return if (AppApplication.connectivityManager.isNetworkAvailable.value == true) {
            KhoWallet.getBalance(
                accountAddress = accountAddress,
                mnemonic = viewModel.mnemonic
            )
        } else
            BigInteger("0")
    }

}
