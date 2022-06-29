package land.kho.meta.presentation.wallet.bsSend

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import land.kho.meta.base.BaseVM
import land.kho.meta.domain.usecases.PreferencesUseCase
import javax.inject.Inject

@HiltViewModel
class BsSendVM @Inject constructor(
    private val preferencesUseCase: PreferencesUseCase
) : BaseVM() {

    lateinit var accountAddress: String

    init {
        getAccountAddress()
    }

    private fun getAccountAddress() {
        viewModelScope.launch {
            preferencesUseCase.getAccountAddress().collect {
                if (!it.isNullOrBlank()) {
                    accountAddress = it
                }
            }
        }
    }
}