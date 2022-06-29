package land.kho.meta.presentation.meta

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import land.kho.meta.base.BaseVM
import land.kho.meta.data.api.NetworkResponse
import land.kho.meta.domain.repository.Repository
import land.kho.meta.domain.usecases.PreferencesUseCase
import land.kho.meta.utils.Event
import land.kho.meta.utils.Utility
import land.kho.wallet.KhoWallet
import javax.inject.Inject

@HiltViewModel
class MetaVM @Inject constructor(
    private val preferencesUseCase: PreferencesUseCase,
    private val repository: Repository
) : BaseVM() {

    lateinit var accountAddress: String
    lateinit var balance: String
    lateinit var unverifiedTokens: String
    var isVerified = false
    val token = MutableLiveData<Event<String>>()

    init {
        getAccountAddress()
        getVerified()
    }

    private fun getAccountAddress() {
        viewModelScope.launch {
            preferencesUseCase.getAccountAddress().collect {
                if (!it.isNullOrBlank()) {
                    accountAddress = it
                    getMnemonic()
                }
            }
        }
    }

    private fun getMnemonic() {
        viewModelScope.launch {
            preferencesUseCase.getMnemonic().collect {
                if (!it.isNullOrBlank()) {
                    getBalance(mnemonic = it)
                }
            }
        }
    }


    private fun getBalance(mnemonic: String) {
        balance = Utility.toAmount(
            amount = KhoWallet.getBalance(
                accountAddress = accountAddress,
                mnemonic = mnemonic
            )
        )
    }

    private fun getVerified() {
        viewModelScope.launch {
            preferencesUseCase.getIsVerified().collect {
                it?.let {
                    isVerified = (it)
                    if (!it){
                        getUnverifiedTokens()
                    }
                }
            }
        }
    }

    private fun getUnverifiedTokens() {
        viewModelScope.launch {
            when (val response = repository.getUnverifiedTokens()) {
                is NetworkResponse.Success -> {
                    unverifiedTokens =response.body!!.count.toString()
                }
                is NetworkResponse.Error -> {
                    Utility.showToast(msg = response.message ?: "")
                }
            }
        }
    }
}
