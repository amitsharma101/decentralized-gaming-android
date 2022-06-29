package land.kho.meta.presentation.home.refer

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import land.kho.meta.R
import land.kho.meta.base.BaseFragment
import land.kho.meta.databinding.FragmentReferBinding
import land.kho.meta.utils.*
import land.kho.meta.utils.Utility.hideKeyboard


@AndroidEntryPoint
class ReferFragment : BaseFragment<FragmentReferBinding, ReferVM>() {

    private val referVM: ReferVM by viewModels()

    override fun layoutRes() = R.layout.fragment_refer

    override fun viewModelClass() = referVM

    override fun bindViews() {
        binding.viewModel = viewModel
        setUpClickListener()
        viewModel.shareImage = BitmapFactory.decodeResource(
            Utility.getResources(),
            R.drawable.share
        )
    }

    private fun setUpClickListener() {

        binding.ivCopy.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.REFER,
                event = AnalyticsConstants.Event.REFERRAL_CODE_COPY,
                value = AnalyticsConstants.Value.COPY_CLICKED
            )
            Utility.copyText(text = viewModel.myReferralCode.value.toString())
        }

        binding.btnShare.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.REFER,
                event = AnalyticsConstants.Event.REFERRAL_SHARED,
                value = AnalyticsConstants.Value.BUTTON_CLICKED
            )
            shareReferral()
        }
        binding.btnReferralCode.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.REFER,
                event = AnalyticsConstants.Event.ENTER_REFERRAL_CODE,
                value = AnalyticsConstants.Value.BUTTON_CLICKED
            )
            binding.btnReferralCode.hideKeyboard()
            val referralCode = binding.etReferralCode.text
            if (!referralCode.isNullOrBlank() && referralCode.length == Constants.REFERRAL_CODE_LENGTH) {
                viewModel.putReferralCode(referralCode = referralCode.toString().uppercase())
            } else {
                binding.etReferralCode.error = "invalid referral Code"
            }
        }
    }

    private fun shareReferral() {
        viewModel.shareImage?.let {
            val uri = QrCodeUtil.shareBitmap(
                bitmap = it,
                context = requireContext(),
                fileName = "share.jpeg"
            )
            val text = resources.getString(
                R.string.share_referral_message,
                viewModel.myReferralCode.value.toString()
            )
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.putExtra(Intent.EXTRA_TEXT, text)
            intent.type = "image/jpeg"
            startActivity(Intent.createChooser(intent, "Share referral"))
        }
    }
}