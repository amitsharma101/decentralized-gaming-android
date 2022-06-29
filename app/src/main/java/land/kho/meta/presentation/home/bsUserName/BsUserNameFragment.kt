package land.kho.meta.presentation.home.bsUserName

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import land.kho.meta.R
import land.kho.meta.base.BaseBS
import land.kho.meta.databinding.FragmentBsUserNameBinding
import land.kho.meta.utils.AnalyticsConstants
import land.kho.meta.utils.AnalyticsLogger
import land.kho.meta.utils.Utility.hideKeyboard

@AndroidEntryPoint
class BsUserNameFragment : BaseBS<FragmentBsUserNameBinding, BsUserNameVM>() {

    private val bsUserNameVM: BsUserNameVM by viewModels()
    private var name: String? = null
    private var isHavingName = false
    lateinit var nameChangeCallback: (name: String) -> Unit

    companion object {
        fun getNewInstance(
            name: String? = null,
            isHavingName: Boolean = false,
            nameChangeCallback: (name: String) -> Unit
        ): BsUserNameFragment {
            return BsUserNameFragment().apply {
                this.name = name
                this.isHavingName = isHavingName
                this.nameChangeCallback = nameChangeCallback
            }
        }
    }

    override fun layoutRes() = R.layout.fragment_bs_user_name

    override fun viewModelClass() = bsUserNameVM


    override fun bindViews() {
        binding.viewModel = viewModel
        viewModel.isHavingName.set(isHavingName)
        viewModel.name = name
        setUpClickListener()
    }

    private fun setUpClickListener() {

        binding.btnSend.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.BS_USER_NAME,
                event = AnalyticsConstants.Event.CHANGE_USER_NAME,
                value = AnalyticsConstants.Value.BUTTON_CLICKED
            )
            binding.btnSend.hideKeyboard()
            if (!binding.etName.text.isNullOrBlank()) {
                viewModel.changeName(name = binding.etName.text.toString()) {
                    nameChangeCallback.invoke(binding.etName.text.toString())
                    dismiss()
                }
            } else {
                binding.etName.error = "invalid email"
            }

        }
    }
}

