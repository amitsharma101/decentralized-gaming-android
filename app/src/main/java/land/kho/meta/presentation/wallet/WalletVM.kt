package land.kho.meta.presentation.wallet

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import land.kho.meta.base.BaseVM
import land.kho.meta.data.api.NetworkResponse
import land.kho.meta.data.model.response.GetProfileDataResponse
import land.kho.meta.data.model.response.Transaction
import land.kho.meta.domain.repository.Repository
import land.kho.meta.domain.usecases.PreferencesUseCase
import land.kho.meta.utils.Event
import land.kho.meta.utils.Utility
import land.kho.wallet.KhoWallet
import javax.inject.Inject

@HiltViewModel
class WalletVM @Inject constructor(
    private val preferencesUseCase: PreferencesUseCase,
    private val repository: Repository
) : BaseVM() {

    lateinit var address: String
    lateinit var mnemonic: String
    var email = ""
    var unverifiedTokens = ""
    val isVerified = MutableLiveData(false)
    val accountAddress = MutableLiveData<Event<String>>()
    val tokenTransactionsList = MutableLiveData<Event<List<Transaction>?>>()
    val nftTransactionsList = MutableLiveData<Event<List<Transaction>?>>()

    init {
        getVerified()
    }

    fun getMnemonic() {
        viewModelScope.launch {
            preferencesUseCase.getMnemonic().collect {
                if (!it.isNullOrBlank()) {
                    mnemonic = it
                    getAccountAddress()
                }
            }
        }
    }

    fun getTokenTransactions() {
        viewModelScope.launch {
            when (val response = repository.getTokenTransactions(
                contractAddress = KhoWallet.getContractAddress(),
                address = address
            )) {
                is NetworkResponse.Success -> {
                    tokenTransactionsList.postValue(Event(content = response.body?.result))
                }
                is NetworkResponse.Error -> {
                    Utility.showToast(msg = response.body?.message ?: "")
                }
            }
        }
    }

    fun getNftTransactions() {
        viewModelScope.launch {
            when (val response = repository.getNftTransactions(
                contractAddress = KhoWallet.nftContractAddress,
                address = address
            )) {
                is NetworkResponse.Success -> {
                    nftTransactionsList.postValue(Event(content = response.body?.result))
                }
                is NetworkResponse.Error -> {
                    Utility.showToast(msg = response.body?.message ?: "")
                }
            }
        }
    }

    private fun getAccountAddress() {
        viewModelScope.launch {
            preferencesUseCase.getAccountAddress().collect {
                if (!it.isNullOrBlank()) {
                    withContext(Dispatchers.Main) {
                        address = it
                        accountAddress.postValue(Event(content = it))
                    }
                }
            }
        }
    }

    private fun getVerified() {
        viewModelScope.launch {
            preferencesUseCase.getIsVerified().collect {
                it?.let {
                    if (it) {
                        isVerified.value = true
                    } else {
                        getMe()
                    }
                }
            }
        }
    }

    private fun getMe() {
        viewModelScope.launch {
            when (val response = repository.getMe()) {
                is NetworkResponse.Success -> {
                    if (response.body!!.verified) {
                        saveInDB(response = response.body) {
                        }
                    } else {
                        getTempEmail()
                        getUnverifiedTokens()
                    }
                }
                is NetworkResponse.Error -> {
                    Utility.showToast(msg = response.message ?: "")
                }
            }
        }
    }

    private fun getUnverifiedTokens() {
        viewModelScope.launch {
            when (val response = repository.getUnverifiedTokens()) {
                is NetworkResponse.Success -> {
                    unverifiedTokens = response.body!!.count.toString()
                }
                is NetworkResponse.Error -> {
                    Utility.showToast(msg = response.message ?: "")
                }
            }
        }
    }

    private fun saveInDB(
        response: GetProfileDataResponse,
        isSaved: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                preferencesUseCase.setEmail(value = response.email ?: "")
                preferencesUseCase.setIsVerified(value = response.verified)
                preferencesUseCase.setUserId(value = response.id)
                preferencesUseCase.setUserName(value = response.nickname)
                preferencesUseCase.setReferralCode(value = response.referralCode)
                preferencesUseCase.setIsReferralCompleted(value = response.isReferralCompleted)
                preferencesUseCase.setReferredBy(value = response.referral?.referrerUser?.referralCode ?: "")
                preferencesUseCase.setNumber(value = response.phoneNumber ?: "")
                withContext(Dispatchers.Main) {
                    isVerified.value = response.verified
                }
                isSaved.invoke(true)
            }
        }
    }

    private fun getTempEmail() {
        viewModelScope.launch {
            preferencesUseCase.getTempEmail().collect {
                if (!it.isNullOrBlank()) {
                    email = it
                }
            }
        }
    }

}
