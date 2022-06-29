package land.kho.meta.presentation.bsInfo

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import land.kho.meta.R
import land.kho.meta.base.BaseBS
import land.kho.meta.databinding.FragmentBsInfoBinding
import land.kho.meta.utils.AnalyticsConstants
import land.kho.meta.utils.AnalyticsLogger
import land.kho.meta.utils.visibleOrGone

@AndroidEntryPoint
class BsInfoFragment : BaseBS<FragmentBsInfoBinding, BsInfoVM>() {

    private val bsInfoVM: BsInfoVM by viewModels()
    private lateinit var infoData: InfoData

    companion object {
        fun getNewInstance(
            infoData: InfoData
        ): BsInfoFragment {
            return BsInfoFragment().apply {
                this.infoData = infoData
            }
        }
    }

    override fun layoutRes() = R.layout.fragment_bs_info

    override fun viewModelClass() = bsInfoVM

    override fun bindViews() {
        binding.viewModel = viewModel
        binding.tvText.text = infoData.text
        binding.btnFirst.visibleOrGone(isVisible = infoData.isButtonVisible)
        binding.btnFirst.text = infoData.buttonText
        binding.btnSecond.visibleOrGone(isVisible = infoData.isButtonSecondVisible)
        binding.btnSecond.text = infoData.buttonSecondText
        setUpClickListener()
    }

    private fun setUpClickListener() {

        binding.btnFirst.setOnClickListener {
            if (infoData.buttonLink.isNotBlank()) {
                AnalyticsLogger.logEvent(
                    screen = AnalyticsConstants.ScreenName.BS_INFO,
                    event = AnalyticsConstants.Event.GO_TO_DISCORD,
                    value = AnalyticsConstants.Value.BUTTON_CLICKED
                )
                openDiscord(url = infoData.buttonLink)
            }
        }
        binding.btnSecond.setOnClickListener {
            if (infoData.buttonSecondLink.isNotBlank()) {
                AnalyticsLogger.logEvent(
                    screen = AnalyticsConstants.ScreenName.BS_INFO,
                    event = AnalyticsConstants.Event.GO_TO_TELEGRAM,
                    value = AnalyticsConstants.Value.BUTTON_CLICKED
                )
                openDiscord(url = infoData.buttonSecondLink)
            }
        }
    }

    private fun openDiscord(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}

