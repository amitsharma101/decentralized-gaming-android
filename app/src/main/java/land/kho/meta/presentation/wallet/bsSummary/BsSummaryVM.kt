package land.kho.meta.presentation.wallet.bsSummary

import androidx.databinding.ObservableField
import dagger.hilt.android.lifecycle.HiltViewModel
import land.kho.meta.base.BaseVM
import land.kho.meta.domain.usecases.PreferencesUseCase
import land.kho.meta.utils.addPropertyChangedCallback
import javax.inject.Inject

@HiltViewModel
class BsSummaryVM @Inject constructor(
    private val preferencesUseCase: PreferencesUseCase
) : BaseVM() {

    var buttonText = ""
    var unverifiedTokens = ""

    val isVerified = ObservableField<Boolean>().apply {
        addPropertyChangedCallback {
            get()?.let {
                buttonText = if (get() == true) "Go to Wallet" else "Go to Wallet and Verify"
            }
        }
    }
}