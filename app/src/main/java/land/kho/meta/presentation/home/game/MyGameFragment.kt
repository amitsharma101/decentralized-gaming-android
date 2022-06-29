package land.kho.meta.presentation.home.game

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import dagger.hilt.android.AndroidEntryPoint
import land.kho.meta.R
import land.kho.meta.base.BaseFragment
import land.kho.meta.databinding.FragmentMyGameBinding
import land.kho.meta.presentation.bsInfo.BsInfoFragment
import land.kho.meta.presentation.bsInfo.InfoData
import land.kho.meta.utils.*
import java.math.BigInteger


@AndroidEntryPoint
class MyGameFragment : BaseFragment<FragmentMyGameBinding, MyGameVM>() {

    private val myGameVM: MyGameVM by viewModels()
    private var pass = 0
    private var khoToken = BigInteger("0")

    override fun layoutRes() = R.layout.fragment_my_game

    override fun viewModelClass() = myGameVM

    override fun bindViews() {
        binding.viewModel = viewModel
        getNavBarData {
            pass = it.pass
            khoToken = it.balance
        }
        viewModel.getToken()
        binding.tvComingSoon.visibleOrGone(isVisible = khoToken <= BigInteger("0"))
        bindObservers()
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner1))
        imageList.add(SlideModel(R.drawable.banner2))
        imageList.add(SlideModel(R.drawable.banner3))
        binding.imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)

        viewModel.syncFcmTokenWithBackendIfNotAlreadySynced()

    }

    private fun bindObservers() {

        viewModel.token.observe(viewLifecycleOwner) { event ->
            event?.getContentIfNotHandled()?.let {
                setUpClickListener()
            }
        }

        viewModel.userStatus.observe(viewLifecycleOwner) { event ->
            event?.getContentIfNotHandled()?.let {
                when (it) {
                    Constants.AVATAR_SELECTION -> {
                        findNavController().navigate(
                            MyGameFragmentDirections.actionMyGameFragmentToAvatarSelectionFragment(
                                viewModel.authToken
                            )
                        )
                    }
                    Constants.META -> {
                        findNavController().navigate(
                            MyGameFragmentDirections.actionMyGameFragmentToMetaFragment(
                                viewModel.authToken
                            )
                        )
                    }
                }
            }
        }
    }

    private fun setUpClickListener() {
        binding.btnClaimReward.setOnClickListener {
            if (khoToken > BigInteger("0")) {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(Constants.DISCORD_LINK))
                startActivity(browserIntent)
            }
            else{
              Utility.showToast(msg = "Please earn more Kho Token")
        }
        }
        binding.cvLudo.setOnClickListener {
            if (pass > 0) {
                AnalyticsLogger.logEvent(
                    screen = AnalyticsConstants.ScreenName.GAME,
                    event = AnalyticsConstants.Event.LUDO_OPENED,
                    value = AnalyticsConstants.Value.BUTTON_CLICKED
                )
                showLoading()
                findNavController().navigate(
                    MyGameFragmentDirections.actionMyGameFragmentToGameFragment(
                        viewModel.authToken
                    )
                )
            } else {
                AnalyticsLogger.logEvent(
                    screen = AnalyticsConstants.ScreenName.GAME,
                    event = AnalyticsConstants.Event.PASS_EXHAUSTED,
                    value = AnalyticsConstants.Value.BUTTON_CLICKED
                )
                BsInfoFragment.getNewInstance(
                    infoData = InfoData(
                        text = "Your game pass has expired, please join Discord or Telegram to get more passes.",
                        isButtonVisible = true,
                        buttonText = "Discord",
                        buttonLink = Constants.DISCORD_LINK,
                        isButtonSecondVisible = true,
                        buttonSecondText = "Telegram",
                        buttonSecondLink = Constants.TELEGRAM_LINK,
                    )
                ).show(childFragmentManager, "BsInfoFragment")
            }
        }
        binding.cvKhoVerse.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.GAME,
                event = AnalyticsConstants.Event.ENTER_KHOVERSE,
                value = AnalyticsConstants.Value.BUTTON_CLICKED
            )
            showLoading()
            viewModel.checkAvatarStatus()
        }
        binding.btnDiscord.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.DISCORD_LINK))
            startActivity(browserIntent)
        }
        binding.btnTelegram.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.TELEGRAM_LINK))
            startActivity(browserIntent)
        }
    }

}